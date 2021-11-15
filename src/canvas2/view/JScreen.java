package canvas2.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JPanel;

import canvas2.core.Drawable;

/**
 * 指定された一つの描画物を描画するパネル。<br>
 * {@link AppWindow}を改造するときに使用できる。
 *
 */
public class JScreen extends JPanel{


	private Drawable obj;
	private boolean isChildVisible = true;


	public JScreen(Drawable obj)
	{

		Objects.requireNonNull(obj);
		this.obj = obj;
		this.setOpaque(false);
	}

	public void setChlidVisible(boolean b)
	{
		this.isChildVisible = b;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g.create();

		this.obj.draw(g2);

		g2.dispose();
	}

	@Override
	protected void paintChildren(Graphics g)
	{
		if(this.isChildVisible)
		{
			super.paintChildren(g);
		}
	}









}

