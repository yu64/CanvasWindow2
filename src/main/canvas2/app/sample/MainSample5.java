package canvas2.app.sample;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.HashSet;

import canvas2.app.App;
import canvas2.core.event.Listener;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.util.ListenerHub;
import canvas2.view.scene.Node;

public class MainSample5 {


	public static void main(String[] args)
	{
		App app = new App();

		EventManager event = app.getEventManager();
		event.add(AWTEvent.class, MouseEvent.MOUSE_MOVED, (Listener<EventObject>)(tpf, e) -> {

			System.out.println(e);
		});

		ListenerHub<AwtListener> hub = new ListenerHub<>(AwtListener.class, new HashSet<>());
		hub.getListeners().add((tpf, awt) -> System.out.println("KeyEvent 1"));
		hub.getListeners().add((tpf, awt) -> System.out.println("KeyEvent 2"));

		event.add(AWTEvent.class, KeyEvent.KEY_TYPED, hub.getHandler());

		MainSample1.testColsed(app);

		app.start();
	}

	public static void testRect(App app, Node node, Dimension size)
	{
		int h = size.height;
		int w = size.width;

		node.add(g2 -> {

			g2.setColor(Color.RED);
			g2.drawRect(0, 0, w, h);
			g2.drawLine(0, 0, w, h);
			g2.drawLine(0, h, w, 0);
		});

	}




}
