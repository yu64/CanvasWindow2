package canvas2.view.scene;

import java.awt.Graphics2D;

import canvas2.AppObject;

@FunctionalInterface
public interface Drawable extends AppObject{

	public void draw(Graphics2D g2);
}
