package canvas2.app.sample;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import canvas2.app.App;
import canvas2.core.debug.TextTree;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.event.flag.KeyFlags;
import canvas2.state.StateTable;
import canvas2.state.obj.StateImpl;
import canvas2.view.scene.Node;
import canvas2.view.scene.TrackingPane;

public class MainSample2 {

	public static void main(String[] args)
	{
		//最初に呼び出すもの。
		App app = new App();

		Node scroll = new Node("scrollNode");
		app.getRootNode().add(scroll);

		Node area = new Node("areaNode");
		scroll.add(area);

		Node overray = new Node("overrayNode");
		app.getRootNode().add(overray);

		Point pos = MainSample2.testMovedCircle(app, area);
		MainSample1.testAxis(app, area);
		MainSample1.testZoom(app, scroll);
		MainSample1.testMove(app, scroll);
		MainSample2.testColsed(app);
		MainSample2.testState(app, overray);
		MainSample2.testMouse(app, area, scroll);
		MainSample2.testSubFrame(app, area, overray, pos);


		System.out.println(app.getClass().getSimpleName() + " TextTree");
		System.out.println(TextTree.getText(app));

		//最後に呼び出すもの
		app.start();
	}


	/**
	 * 回る円を描画
	 */
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


	/**
	 * キーによってアプリケーションを閉じる。<br>
	 * キー: <br>
	 * ESC
	 */
	public static void testColsed(App app)
	{
		EventManager event = app.getEventManager();

		//エスケープキーで終了。
		event.add(AWTEvent.class, KeyEvent.KEY_PRESSED, (t, v) -> {

			KeyEvent e = (KeyEvent) v;

			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				app.close();
			}
		});

		//ウィンドウを閉じたら、終了。
		event.add(AWTEvent.class, WindowEvent.WINDOW_CLOSING, (t, v) -> {

			app.close();
		});

	}

	/**
	 * 状態変化を行う。<br>
	 * キー: <br>
	 * 1, 2, 3
	 */
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
		table.set(stateA, stateB, () -> flag.isPressed(KeyEvent.VK_1));
		table.set(stateB, stateC, () -> flag.isPressed(KeyEvent.VK_2));
		table.set(stateC, stateA, () -> flag.isPressed(KeyEvent.VK_3));
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

		System.out.println(table.getClass().getSimpleName() + " TextTree");
		System.out.println(TextTree.getText(table));
	}


	/**
	 * マウスで左クリックした位置に円を描画する。<br>
	 */
	public static void testMouse(App app, Node area, Node scroll)
	{
		EventManager event = app.getEventManager();

		Point pos = new Point();

		AwtListener action = (tpf, v) -> {

			MouseEvent e = (MouseEvent) v;

			Point screenPos = e.getPoint();
			pos.setLocation(scroll.getTransform().inverseTransform(screenPos, pos));

		};

		event.add(AWTEvent.class, MouseEvent.MOUSE_DRAGGED, action);
		event.add(AWTEvent.class, MouseEvent.MOUSE_PRESSED, action);
		event.add(AWTEvent.class, MouseEvent.MOUSE_RELEASED, action);

		area.add(g2 -> {

			g2.setColor(Color.MAGENTA);
			g2.fillOval(pos.x - 10, pos.y - 10, 20, 20);

		});

	}

	/**
	 * 指定した座標を追跡するサブウィンドウを描画する。
	 */
	public static void testSubFrame(App app, Node area, Node overray, Point pos)
	{
		TrackingPane sub = new TrackingPane("subFrame", "sub", area);
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
