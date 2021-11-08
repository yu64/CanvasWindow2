package canvas2;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import canvas2.debug.TextTree;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.state.StateImpl;
import canvas2.state.StateTable;
import canvas2.util.TransformUtil;
import canvas2.value.KeyFlags;
import canvas2.value.file.LoaderManager;
import canvas2.value.file.ReadTicket;
import canvas2.value.file.test.TestObject1;
import canvas2.value.file.test.TestObject2;
import canvas2.value.file.test.TestReader2;
import canvas2.view.scene.Node;
import canvas2.view.scene.TrackingArea;

public class MainTest {

	public static void main(String[] args)
	{
		App app = new App();
		app.start();

		Node scroll = new Node("scrollNode");
		app.getRootNode().add(scroll);

		Node area = new Node("areaNode");
		scroll.add(area);

		Node overray = new Node("overrayNode");
		app.getRootNode().add(overray);

		Point pos = MainTest.testMovedCircle(app, area);
		MainTest.testAxis(app, area);
		MainTest.testZoom(app, scroll);
		MainTest.testMove(app, scroll);
		MainTest.testColsed(app);
		MainTest.testState(app, overray);
		MainTest.testLoad(app, area);
		MainTest.testMouse(app, area, scroll);
		MainTest.testSubFrame(app, area, overray, pos);

		System.out.println(TextTree.getText(app));


	}



	public static Point testMovedCircle(App app, Node node)
	{

		float[] value = {0};
		Point pos = new Point();

		//円の座標(動き)を更新
		app.getLogic().add(tpf -> {

			value[0] += 1 * tpf;

			if(value[0] >= 360)
			{
				value[0] = 0;
			}

			double r = Math.toRadians(value[0]);
			int size = 100;

			pos.x = (int) (Math.sin(r) * size);
			pos.y = (int) (Math.cos(r) * size);

		});

		//動く円の描画
		node.add(g2 -> {

			g2.setColor(Color.BLUE);
			g2.fillOval(pos.x, pos.y, 10, 10);

		});


		return pos;
	}

	public static void testAxis(App app, Node node)
	{
		//座標軸の描画
		node.add(g2 -> {

			g2.setColor(Color.RED);
			g2.drawLine(-1000, 0, 1000, 0);

			g2.setColor(Color.BLUE);
			g2.drawLine(0, -1000, 0, 1000);


		});
	}

	public static void testZoom(App app, Node node)
	{
		node.add(g2 -> {

			g2.setColor(Color.DARK_GRAY);
			g2.drawOval(480 - 10, 270 - 10, 20, 20);

			g2.drawString(node.getName() + ": (480, 270)", 480, 270 + 30);
		});

		EventManager event = app.getEventManager();
		event.add(AwtListener.class, KeyEvent.KEY_RELEASED, (tpf, v) -> {

			KeyEvent e = (KeyEvent) v;

			if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				TransformUtil.scale(node.getTransform(), 0.5F, 480, 270);
			}

			if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				TransformUtil.scale(node.getTransform(), 1 / 0.5F, 480, 270);
			}
		});

	}

	public static void testMove(App app, Node node)
	{
		EventManager event = app.getEventManager();

		Set<Integer> keys = new HashSet<>();
		keys.add(KeyEvent.VK_A);
		keys.add(KeyEvent.VK_D);
		keys.add(KeyEvent.VK_W);
		keys.add(KeyEvent.VK_S);

		KeyFlags flag = new KeyFlags(keys);
		flag.registerTo(event);

		app.getLogic().add(tpf -> {

			float speed = 1.0F * tpf;

			float x = 0.0F;
			float y = 0.0F;

			if(flag.isPressed(KeyEvent.VK_A))
			{
				x += speed;
			}

			if(flag.isPressed(KeyEvent.VK_D))
			{
				x += -speed;
			}

			if(flag.isPressed(KeyEvent.VK_W))
			{
				y += speed;
			}

			if(flag.isPressed(KeyEvent.VK_S))
			{
				y += -speed;
			}

			node.getTransform().translate(x, y);

		});

		node.add(g2 -> {

			g2.setColor(Color.BLUE);
			g2.drawString(node.getName() + ": " + node.getTransform(), 50, 70);
		});
	}

	public static void testColsed(App app)
	{
		EventManager event = app.getEventManager();

		//エスケープキーで終了。
		event.add(AwtListener.class, KeyEvent.KEY_PRESSED, (t, v) -> {

			KeyEvent e = (KeyEvent) v;

			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				app.close();
			}
		});

		//ウィンドウを閉じたら、終了。
		event.add(AwtListener.class, WindowEvent.WINDOW_CLOSING, (t, v) -> {

			app.close();
		});

	}

	public static void testState(App app, Node node)
	{
		EventManager event = app.getEventManager();


		Set<Integer> keys = new HashSet<>();
		keys.add(KeyEvent.VK_1);
		keys.add(KeyEvent.VK_2);
		keys.add(KeyEvent.VK_3);

		KeyFlags flag = new KeyFlags(keys);
		flag.registerTo(event);


		StateImpl stateA = new StateImpl();
		StateImpl stateB = new StateImpl();
		StateImpl stateC = new StateImpl();

		StateTable<StateImpl> table = new StateTable<>(stateA);
		table.register(stateA, stateB, () -> flag.isPressed(KeyEvent.VK_1));
		table.register(stateB, stateC, () -> flag.isPressed(KeyEvent.VK_2));
		table.register(stateC, stateA, () -> flag.isPressed(KeyEvent.VK_3));
		app.getLogic().add(table);

		node.add(g2 -> {



			if(table.getState() == stateA)
			{
				g2.drawString("stateA", 50, 500);
			}

			if(table.getState() == stateB)
			{
				g2.drawString("stateB", 50, 500);
			}

			if(table.getState() == stateC)
			{
				g2.drawString("stateC", 50, 500);
			}

		});

		System.out.println(TextTree.getText(table));
	}

	public static void testLoad(App app, Node node)
	{
		LoaderManager loader = new LoaderManager();
		app.getLogic().add(loader);

		loader.register(new TestReader2());

		String path1 = "canvas2/value/file/test/testFile1.txt";
		String path2 = "canvas2/value/file/test/testFile2.txt";

		ReadTicket<TestObject1> ticket1 = loader.load(TestObject1.class, path1);
		ReadTicket<TestObject1> ticket2 = loader.load(TestObject1.class, path1);
		ReadTicket<TestObject2> ticket3 = loader.load(TestObject2.class, path2);


		node.add(g2 -> {

			TestObject1 obj1 = ticket1.getResult();
			TestObject1 obj2 = ticket2.getResult();
			TestObject2 obj3 = ticket3.getResult();

			if(obj1 != null)
			{
				g2.drawString(obj1.getText(), 100, 100);
			}

			if(obj2 != null)
			{
				g2.drawString(obj2.getText(), 100, 120);
			}

			if(obj3 != null)
			{
				g2.drawString(obj3.getText(), 100, 140);
			}

		});

	}


	private static void testMouse(App app, Node area, Node scroll)
	{
		EventManager event = app.getEventManager();

		Point pos = new Point();

		AwtListener action = (tpf, v) -> {

			MouseEvent e = (MouseEvent) v;

			Point screenPos = e.getPoint();
			pos.setLocation(scroll.getTransform().inverseTransform(screenPos, pos));

		};

		event.add(MouseEvent.MOUSE_DRAGGED, action);
		event.add(MouseEvent.MOUSE_PRESSED, action);
		event.add(MouseEvent.MOUSE_RELEASED, action);

		area.add(g2 -> {

			g2.setColor(Color.MAGENTA);
			g2.fillOval(pos.x - 10, pos.y - 10, 20, 20);

		});

	}

	private static void testSubFrame(App app, Node area, Node overray, Point pos)
	{
		TrackingArea sub = new TrackingArea("subFrame", "sub", area);
		overray.add(sub);

		Rectangle rect = new Rectangle(0, 0, 200, 200);

		sub.setShape(rect);
		sub.getTransform().translate(20, 20);
		sub.setBackground(g2 -> {

			g2.setColor(Color.LIGHT_GRAY);
			g2.fill(rect);
		});

		app.getLogic().add(tpf -> {

			sub.track(pos, true);
		});


	}

}
