package canvas2.view.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import canvas2.util.resizer.FitResizer;
import canvas2.util.resizer.Resizer;

public class JResizePane extends JPanel{

	private Component target;
	private Resizer resizer;
	private Dimension targetSize;

	public JResizePane(Component target, Dimension targetSize)
	{
		this.target = target;
		this.targetSize = targetSize;

		this.add(target);
		this.setLayout(null);
		this.addComponentListener(new ResizeListener());
		this.setResizer(new FitResizer());

	}

	public void setResizer(Resizer resizer)
	{
		this.resizer = resizer;
	}


	protected class ResizeListener extends ComponentAdapter{

		@Override
		public void componentResized(ComponentEvent e)
		{
			JResizePane pane = JResizePane.this;

			Dimension parentSize = pane.getSize();
			Dimension childSize = pane.targetSize;

			Resizer r = pane.resizer;
			r.update(parentSize, childSize);

			pane.target.setBounds(
					r.getX(),
					r.getY(),
					r.getResizedWidth(),
					r.getResizedHeight()
					);

		}
	}

}
