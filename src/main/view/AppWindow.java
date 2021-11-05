package main.view;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JFrame;

import main.logic.Updatable;
import main.view.scene.Drawable;
import main.view.scene.Node;

/**
 * アプリケーションのGUI。<br>
 * 描画物を追加するときは、ルートノードに追加すること。<br>
 * 描画するために更新する必要がある。
 *
 */
public class AppWindow extends JFrame implements Updatable{

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
		this.screen.setPreferredSize(size);
		this.screen.setFocusable(true);

		this.add(this.screen);
		this.pack();

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

	public Node getRoot()
	{
		return this.root;
	}

	@Override
	public void update(float tpf)
	{
		this.repaint();
	}




}
