package canvas2.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JPanel;

import canvas2.core.Drawable;

/**
 * 指定された一つの描画物を描画するパネル。<br>
 * {@link AppWindow}で使用される。
 *
 */
public class JScreen extends JPanel{


	private Drawable obj;
	private boolean isChildVisible = true;
	private String name;

	public JScreen(Drawable obj)
	{
		this("No Name", obj);
	}

	public JScreen(String name, Drawable obj)
	{
		Objects.requireNonNull(obj);
		this.obj = obj;
		this.name = name;

		this.setOpaque(false);
		this.enableEvents();
		this.setLayout(new BorderLayout());
	}

	public void setDrawable(Drawable obj)
	{
		Objects.requireNonNull(obj);
		this.obj = obj;
	}

	public Drawable getDrawable()
	{
		return this.obj;
	}


	public void setChlidVisible(boolean b)
	{
		this.isChildVisible = b;
	}


	public void enableEvents()
	{
		super.enableEvents(0xFFFF_FFFF_FFFF_FFFFL);
		return;
	}

	public void disableEvents()
	{
		super.disableEvents(0xFFFF_FFFF_FFFF_FFFFL);
		return;
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

	@Override
	public String toString()
	{
		return this.getClass().getCanonicalName() + "[" + this.name + "]";
	}




}

