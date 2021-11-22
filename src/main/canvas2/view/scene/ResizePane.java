package canvas2.view.scene;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;

import canvas2.util.resizer.FitResizer;
import canvas2.util.resizer.Resizer;

public class ResizePane extends Pane {

	private Node between;
	private Resizer resizer;

	public ResizePane(String outer, String inner)
	{
		this(outer, "Resized Margin", inner);
	}

	public ResizePane(String outer, String between, String inner)
	{
		super(outer, inner);
		this.between = this.createNode(inner);
		this.resizer = new FitResizer();
		this.initNodeLink();
	}

	@Override
	protected void initNodeLink()
	{
		if(this.between == null)
		{
			return;
		}

		this.add(this.between);
		this.between.add(this.getInnerNode());
	}

	public Node getResizedNode()
	{
		return this.between;
	}


	public void resize(Dimension outerSize, Dimension innerSize)
	{
		this.resizer.update(outerSize, innerSize);

		AffineTransform t = this.getTransform();
		synchronized (t)
		{
			t.setToIdentity();
			t.translate(
					this.resizer.getX(),
					this.resizer.getY()
					);

			t.scale(
					this.resizer.getScaleX(),
					this.resizer.getScaleY()
					);

		}


	}





}
