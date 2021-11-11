package canvas2.sample;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import canvas2.App;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.util.TransformUtil;
import canvas2.value.KeyFlags;
import canvas2.view.scene.Node;

public class MainSample1 {


	public static void main(String[] args)
	{
		App app = new App();

		Node scroll = new Node("scrollNode");
		app.getRootNode().add(scroll);

		Node area = new Node("areaNode");
		scroll.add(area);

		Node overray = new Node("overrayNode");
		app.getRootNode().add(overray);

		MainSample1.testAxis(app, area);
		MainSample1.testZoom(app, scroll);
		MainSample1.testMove(app, scroll);

		app.start();
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
}
