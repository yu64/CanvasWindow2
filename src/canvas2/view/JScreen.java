package canvas2.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JPanel;

import canvas2.view.scene.Drawable;

/**
 * 指定され一つの描画物を描画するパネル。<br>
 * {@link AppWindow}を改造するときに使用できる。
 *
 */
public class JScreen extends JPanel{


	private Drawable obj;

	public JScreen(Drawable obj)
	{
		Objects.requireNonNull(obj);

		this.obj = obj;

	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g.create();

		this.obj.draw(g2);


		g2.dispose();

	}







}
