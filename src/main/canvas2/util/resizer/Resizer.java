package canvas2.util.resizer;

import java.awt.Dimension;

public interface Resizer {

	public void update(Dimension parent, Dimension child);

	public float getScaleX();
	public float getScaleY();
	public int getX();
	public int getY();
	public int getResizedWidth();
	public int getResizedHeight();


}
