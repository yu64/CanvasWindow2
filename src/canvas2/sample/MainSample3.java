package canvas2.sample;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;

import canvas2.App;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.logic.AppLogic;
import canvas2.util.Pool;
import canvas2.view.scene.Node;

public class MainSample3 {


	public static void main(String[] args)
	{
		App app = new App();
		AppLogic logic = app.getLogic();
		Deque<Entity> entitys = new ArrayDeque<>();
		Point2D mouse = new Point2D.Double();

		Pool pool = new Pool();
		pool.register(
				Entity.class,
				Entity::new,
				o -> o.reset(),
				1
				);




		BitSet flags = new BitSet();


		double force = 100.0D;
		logic.add(tpf -> {

			for(Entity e : entitys)
			{
				Point2D.Double vec = e.vec;
				Point2D.Double pos = e.pos;

				double x = mouse.getX() - pos.x;
				double y = mouse.getY() - pos.y;

				if(flags.get(1))
				{
					double d = mouse.distance(pos);

					if(x != 0 || y != 0)
					{
						vec.x = x / d;
						vec.y = y / d;

						e.acc = force / (d * d);
					}
					else
					{
						e.acc = 0;
						e.speed = 0;
					}

				}



				e.speed += e.acc;

				if(300 < e.speed)
				{
					e.speed = 300;
				}

				e.speed = e.speed * 0.96D;


				pos.x += vec.x * e.speed * tpf;
				pos.y += vec.y * e.speed * tpf;

				Dimension size = app.getWindow().getScreenSize();
				double w = size.getWidth();
				double h = size.getHeight();

				pos.x = (0 < pos.x ? pos.x : 0);
				pos.y = (0 < pos.y ? pos.y : 0);
				pos.x = (pos.x < w ? pos.x : w);
				pos.y = (pos.y < h ? pos.y : h);

			}


		});



		Node root = app.getRootNode();
		root.add(g2 -> {

			g2.setColor(Color.BLUE);
			for(Entity e : entitys)
			{
				Point2D.Double p = e.pos;

				double r = 10.0F;
				double x = p.x - r;
				double y = p.y - r;

				g2.fillOval(
						(int)x,
						(int)y,
						(int)r * 2,
						(int)r * 2
						);
			}

		});


		EventManager event = app.getEventManager();
		event.add(AwtListener.class, WindowEvent.WINDOW_CLOSED, (tpf, awt) -> {

			app.close();
		});

		event.add(AwtListener.class, MouseEvent.MOUSE_RELEASED, (tpf, awt) -> {

			if( !(awt instanceof MouseEvent) )
			{
				return;
			}

			MouseEvent e = (MouseEvent) awt;

			if(e.getButton() == MouseEvent.BUTTON1)
			{
				flags.set(1);
			}

			if(e.getButton() == MouseEvent.BUTTON3)
			{
				flags.clear(1);
			}

		});

		event.add(AwtListener.class, MouseEvent.MOUSE_MOVED, (tpf, awt) -> {

			if( !(awt instanceof MouseEvent) )
			{
				return;
			}

			MouseEvent e = (MouseEvent) awt;
			mouse.setLocation(e.getPoint());
		});

		event.add(AwtListener.class, MouseWheelEvent.MOUSE_WHEEL, (tpf, awt) -> {

			if( !(awt instanceof MouseWheelEvent) )
			{
				return;
			}

			MouseWheelEvent e = (MouseWheelEvent) awt;

			int r = e.getWheelRotation();
			if(0 < r)
			{
				Entity entity = entitys.pollFirst();
				pool.free(entity);
			}
			else if(r < 0)
			{
				Entity obj = pool.obtain(Entity.class);
				obj.pos.setLocation(mouse);
				entitys.add(obj);
			}

		});

		app.start();
	}

	private static class Entity
	{
		private Point2D.Double pos = new Point2D.Double();
		private Point2D.Double vec = new Point2D.Double();
		private double speed = 0;
		private double acc = 0;

		private void reset()
		{
			this.pos.setLocation(0, 0);
			this.vec.setLocation(0, 0);
			this.speed = 0;
			this.acc = 0;

		}


	}
}
