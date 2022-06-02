package canvas2.app.sample;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import canvas2.app.App;
import canvas2.core.debug.TextTree;
import canvas2.event.EventManager;
import canvas2.event.flag.KeyFlags;
import canvas2.util.NamedLambda;
import canvas2.util.TransformUtil;
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

		MainSample1.settingAxis(app, area);
		MainSample1.settingZoom(app, scroll);
		MainSample1.settingMove(app, scroll);
		MainSample1.settingColsed(app);

		app.start();
		
		System.out.println(app.getClass().getSimpleName() + " TextTree");
		System.out.println(TextTree.getText(app));
	}



	/**
	 * 座標軸を描画
	 */
	public static void settingAxis(App app, Node node)
	{
		//座標軸の描画
		node.add(NamedLambda.wrap("axis", g2 -> {

			g2.setColor(Color.RED);
			g2.drawLine(-1000, 0, 1000, 0);

			g2.setColor(Color.BLUE);
			g2.drawLine(0, -1000, 0, 1000);


		}));
	}

	/**
	 * キーによるズーム<br>
	 * キー: <br>
	 * UP, DOWN
	 */
	public static void settingZoom(App app, Node node)
	{
		node.add(NamedLambda.wrap("zoomPoint", g2 -> {

			g2.setColor(Color.DARK_GRAY);
			g2.drawOval(480 - 10, 270 - 10, 20, 20);

			g2.drawString(node.getName() + ": (480, 270)", 480, 270 + 30);
		}));

		EventManager event = app.getEventManager();
		event.add(
				AWTEvent.class, 
				KeyEvent.KEY_RELEASED, 
				NamedLambda.wrap("zoomKey", (tpf, v) -> {
					
					KeyEvent e = (KeyEvent) v;
		
					if(e.getKeyCode() == KeyEvent.VK_UP)
					{
						TransformUtil.scale(node.getTransform(), 0.5F, 480, 270);
					}
		
					if(e.getKeyCode() == KeyEvent.VK_DOWN)
					{
						TransformUtil.scale(node.getTransform(), 1 / 0.5F, 480, 270);
					}
				})
		);

	}

	/**
	 * キーによるスクロール<br>
	 * キー: <br>
	 * A, D, W, S
	 */
	public static void settingMove(App app, Node node)
	{
		EventManager event = app.getEventManager();

		Set<Integer> keys = new HashSet<>();
		keys.add(KeyEvent.VK_A);
		keys.add(KeyEvent.VK_D);
		keys.add(KeyEvent.VK_W);
		keys.add(KeyEvent.VK_S);

		KeyFlags flag = new KeyFlags(keys);
		flag.registerTo(event);

		app.getLogic().add(NamedLambda.wrap("updateScroll", tpf -> {

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

		}));

		node.add(NamedLambda.wrap("scrollState", g2 -> {

			g2.setColor(Color.BLUE);
			g2.drawString(node.getName() + ": " + node.getTransform(), 50, 70);
		}));
	}


	
	/**
	 * キーによってアプリケーションを閉じる。<br>
	 * キー: <br>
	 * ESC
	 */
	public static void settingColsed(App app)
	{
		EventManager event = app.getEventManager();

		//エスケープキーで終了。
		event.add(
				AWTEvent.class, 
				KeyEvent.KEY_PRESSED, 
				NamedLambda.wrap("closedKey", (t, v) -> {

					KeyEvent e = (KeyEvent) v;
			
					if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						app.close();
					}
				})
		);

		//ウィンドウを閉じたら、終了。
		event.add(
				AWTEvent.class, 
				WindowEvent.WINDOW_CLOSING,
				NamedLambda.wrap("free", (t, v) -> {

					app.close();
				})
		);

	}
	
	
}
