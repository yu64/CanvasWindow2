package canvas2.view;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JFrame;

import canvas2.core.Drawable;
import canvas2.core.Updatable;
import canvas2.debug.TextTree;
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
		this(null, new Dimension(960, 540), obj);
	}

	public AppWindow(Dimension size, Node obj)
	{
		this(null, size, obj);
	}

	public AppWindow(Point pos, Dimension size, Node obj)
	{
		this.init(pos, size, obj);
	}

	protected void init(Point pos, Dimension size, Node obj)
	{
		Objects.requireNonNull(size);
		Objects.requireNonNull(obj);

		this.root = obj;

		this.screen = this.createScreen(obj);
		this.screen.setFocusable(true);

		this.add(this.screen);
		this.setScreenSize(size);

		this.screen.requestFocusInWindow();


		if(pos == null)
		{
			this.setLocationRelativeTo(null);
		}
		else
		{
			this.getLocation(pos);
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

		//this.createTreeText(sb, nest + 1);


		return sb;
	}




}
