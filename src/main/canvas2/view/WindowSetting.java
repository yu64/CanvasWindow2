package canvas2.view;

import java.awt.Dimension;
import java.awt.Point;

public class WindowSetting {

	private Point pos;
	private Dimension screenSize;


	public WindowSetting(Point pos, Dimension screenSize)
	{
		this.pos = pos;
		this.screenSize = screenSize;
	}


	public Point getPos()
	{
		return pos;
	}


	public Dimension getScreenSize()
	{
		return screenSize;
	}



}
