package canvas2.view.swing;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;

import canvas2.core.Drawable;
import canvas2.view.JScreen;
import canvas2.view.scene.Node;

@Deprecated
public class JResizableScreen extends JScreen{
	
	public Node node = null;

	
	{
		this.addComponentListener(new ResizedListener());
	}
	
	
	public JResizableScreen(Drawable obj)
	{
		this("No Name", obj);
	}
	
	public JResizableScreen(String name, Drawable obj)
	{
		super(name, obj);

		if(obj instanceof Node)
		{
			this.node = (Node) obj;
			return;
		}
		
		this.node = this.createNode();
		this.node.add(obj);
	}

	
	protected Node createNode()
	{
		return new Node("implicitFrameToScreenResizing");
	}


	protected class ResizedListener extends ComponentAdapter{

		private Dimension prev = new Dimension();

		
		
		@Override
		public void componentResized(ComponentEvent e)
		{
			Dimension next = JResizableScreen.this.getSize();
			Dimension prev = this.prev;
			
			if(next.equals(prev))
			{
				return;
			}
			
			if(prev.width == 0 && prev.height == 0)
			{
				this.prev.setSize(next);
				return;
			}
			
			float scaleX = (float)next.width / prev.width;
			float scaleY = (float)next.height / prev.height;
			
			scaleX = Math.min(scaleX, scaleY);
			scaleY = Math.min(scaleX, scaleY);
			
			
			System.out.println(scaleX + " " + scaleY);
			
			Node node = JResizableScreen.this.node;
			AffineTransform t = node.getTransform();
			t.scale(scaleX, scaleY);
			
			this.prev.setSize(next);
		}

	}

}
