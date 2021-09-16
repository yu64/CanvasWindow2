package main.view.scene;

import java.awt.Graphics2D;

import main.AppObject;

@FunctionalInterface
public interface Drawable extends AppObject{

	public void draw(Graphics2D g2);
}
