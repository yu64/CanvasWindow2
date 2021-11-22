package canvas2.view;

import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JFrame;

import canvas2.core.Drawable;
import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.view.scene.Node;

/**
 * アプリケーションのGUI。<br>
 * 描画物を追加するときは、ルートノードに追加すること。<br>
 * 描画するために更新する必要がある。
 *
 */
public class AppWindow extends JFrame implements Updatable, TextTree{

	private JScreen screen;
	private Node root;

	public AppWindow(Node obj)
	{
		this(obj, new WindowSetting(null, new Dimension(960, 540)));
	}

	public AppWindow(Node obj, WindowSetting s)
	{
		this.init(s, obj);
	}


	protected void init(WindowSetting s, Node obj)
	{
		Objects.requireNonNull(s);
		Objects.requireNonNull(obj);

		this.root = obj;

		this.screen = this.createScreen(obj);
		this.screen.setFocusable(true);

		this.add(this.screen);
		this.setScreenSize(s.getScreenSize());

		this.screen.requestFocusInWindow();


		if(s.getPos() == null)
		{
			this.setLocationRelativeTo(null);
		}
		else
		{
			this.getLocation(s.getPos());
		}

	}

	protected JScreen createScreen(Drawable obj)
	{
		return new JScreen(obj);
	}

	public JScreen getScreen()
	{
		return this.screen;
	}

	public void setScreenSize(Dimension size)
	{
		if(size == null)
		{
			Dimension s = this.screen.getPreferredSize();
			s.setSize(0, 0);
			this.pack();
			return;
		}

		this.screen.setPreferredSize(size);
		this.pack();
	}

	public Dimension getScreenSize()
	{
		return this.screen.getSize();
	}

	public Node getRoot()
	{
		return this.root;
	}

	@Override
	public void update(float tpf)
	{
		this.repaint();
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String e = System.lineSeparator();
		String t0 = "\t".repeat(nest);

		sb.append(t0);
		sb.append(this.getClass().getSimpleName());
		sb.append(e);


		return sb;
	}




}
