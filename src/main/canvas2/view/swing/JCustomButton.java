package canvas2.view.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

import canvas2.core.Drawable;

public class JCustomButton extends JButton{


	private boolean canPaint = true;
	private Drawable back;
	private Drawable clicked;
	private Drawable hover;
	
	{
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}

	public JCustomButton()
	{
		super();
	}

	public JCustomButton(Action a)
	{
		super(a);
	}

	public JCustomButton(Icon icon)
	{
		super(icon);
	}

	public JCustomButton(String text, Icon icon)
	{
		super(text, icon);
	}

	public JCustomButton(String text)
	{
		super(text);
	}
	
	
	public void setCustomPainted(boolean canPaint)
	{
		this.canPaint = canPaint;
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
	
	protected void paintCustom(Graphics g)
	{
		if(!this.canPaint)
		{
			return;
		}
		
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
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		this.paintCustom(g);
		super.paintComponent(g);
	}

	@Override
	protected void paintBorder(Graphics g)
	{
		super.paintBorder(g);
	}

	
	
}
