package canvas2.util.resizer;

import java.awt.Dimension;

public class FitResizer implements Resizer{

	private float scale = 1.0F;
	private int x = 0;
	private int y = 0;
	private int resizedWidth;
	private int resizedHeight;


	@Override
	public void update(Dimension outerSize, Dimension innerSize)
	{
		float scaleX = (float)outerSize.width / innerSize.width;
		float scaleY = (float)outerSize.height / innerSize.height;

		this.scale = Math.min(scaleX, scaleY);

		this.resizedWidth = Math.round(innerSize.width * this.scale);
		this.resizedHeight = Math.round(innerSize.height * this.scale);

		int betweenWidth = outerSize.width - this.resizedWidth;
		int betweenHeight = outerSize.height - this.resizedHeight;

		this.x = betweenWidth / 2;
		this.y = betweenHeight / 2;

	}

	@Override
	public float getScaleX()
	{
		return this.scale;
	}

	@Override
	public float getScaleY()
	{
		return this.scale;
	}

	@Override
	public int getX()
	{
		return this.x;
	}

	@Override
	public int getY()
	{
		return this.y;
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
