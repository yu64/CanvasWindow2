package canvas2.view.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

import canvas2.core.Drawable;

public class JButtonScreen extends JButton{

	private Drawable back;
	private Drawable clicked;
	private Drawable hover;
	
	{
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}

	public JButtonScreen()
	{
		super();
	}

	public JButtonScreen(Action a)
	{
		super(a);
	}

	public JButtonScreen(Icon icon)
	{
		super(icon);
	}

	public JButtonScreen(String text, Icon icon)
	{
		super(text, icon);
	}

	public JButtonScreen(String text)
	{
		super(text);
	}
	
	public void setBackground(Drawable back)
	{
		this.back = back;
	}
	
	public void setHover(Drawable hover)
	{
		this.hover = hover;
	}
	
	public void setClicked(Drawable clicked)
	{
		this.clicked = clicked;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		if(this.back != null)
		{
			this.back.draw(g2);
		}
		
		ButtonModel model = this.getModel();
		if(model.isRollover() && this.hover != null)
		{
			this.hover.draw(g2);
		}
		
		if(model.isPressed() && this.clicked != null)
		{
			this.clicked.draw(g2);
		}
		
		
		
		super.paintComponent(g);
	}

	@Override
	protected void paintBorder(Graphics g)
	{
		// TODO 自動生成されたメソッド・スタブ
		super.paintBorder(g);
	}

	
	
}
