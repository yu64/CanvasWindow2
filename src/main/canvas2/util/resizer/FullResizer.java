package canvas2.util.resizer;

import java.awt.Dimension;

public class FullResizer implements Resizer{

	private float scaleY = 1.0F;
	private float scaleX = 1.0F;
	private int resizedWidth;
	private int resizedHeight;


	@Override
	public void update(Dimension outerSize, Dimension innerSize)
	{
		this.scaleX = (float)outerSize.width / innerSize.width;
		this.scaleY = (float)outerSize.height / innerSize.height;

		this.resizedWidth = Math.round(innerSize.width * this.scaleX);
		this.resizedHeight = Math.round(innerSize.height * this.scaleY);

	}

	@Override
	public float getScaleX()
	{
		return this.scaleX;
	}

	@Override
	public float getScaleY()
	{
		return this.scaleY;
	}

	@Override
	public int getX()
	{
		return 0;
	}

	@Override
	public int getY()
	{
		return 0;
	}

	@Override
	public int getResizedWidth()
	{
		return this.resizedWidth;
	}

	@Override
	public int getResizedHeight()
	{
		return this.resizedHeight;
	}

}
