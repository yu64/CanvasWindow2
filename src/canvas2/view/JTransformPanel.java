package canvas2.view;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import canvas2.core.Updatable;

public class JTransformPanel extends JScrollPane implements Updatable{

	private AffineTransform syncSrc;
	private JPanel innerPanel;

	public JTransformPanel(AffineTransform syncSrc)
	{
		super(
				new JPanel(),
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS

				);
		this.syncSrc = syncSrc;
		this.innerPanel = (JPanel) this.getViewport().getView();

		this.innerPanel.setOpaque(false);
		this.innerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		this.innerPanel.setLayout(null);

		this.setOpaque(false);
		this.getViewport().setOpaque(false);
	}



	public JPanel getInnerPanel()
	{
		return this.innerPanel;
	}

	@Override
	public void update(float tpf) throws Exception
	{
		double x = -this.syncSrc.getTranslateX();
		double y = -this.syncSrc.getTranslateY();

		if(x < 0)
		{
			x = 0;
		}

		if(y < 0)
		{
			y = 0;
		}

		this.getHorizontalScrollBar().setValue((int) x);
		this.getVerticalScrollBar().setValue((int) y);


	}


}
