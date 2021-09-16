package main.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JPanel;

import main.view.scene.Drawable;

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
