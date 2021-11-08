package canvas2.view.scene;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

import canvas2.debug.TextTree;
import canvas2.view.AppWindow;

/**
 * シーングラフのノードを示すクラス。<br>
 * 描画するときは、{@link AppWindow}のルートノードに登録する必要がある。
 *
 */
public class Node implements Drawable, TextTree{

	private Node parent = null;
	private Collection<Drawable> child;
	private String name;
	private AffineTransform transform = new AffineTransform();


	public Node(String name)
	{
		this.child = this.createCollection();
		this.name = name;
	}

	protected Collection<Drawable> createCollection()
	{
		return new LinkedHashSet<>();
	}


	public String getName()
	{
		return this.name;
	}

	public AffineTransform getTransform()
	{
		return this.transform;
	}



	public void setParent(Node parent)
	{
		this.removeParent();

		parent.child.add(this);
		this.parent = parent;
	}

	public void removeParent()
	{
		if(this.parent == null)
		{
			return;
		}

		this.parent.child.remove(this);
		this.parent = null;
	}



	/**
	 * 配下の描画物を追加する。<br>
	 * 後から追加された物は、描画上の優先順位が高い。
	 *
	 */
	public void add(Drawable d)
	{
		Objects.requireNonNull(d);
		this.child.add(d);

		if(d instanceof Node)
		{
			Node n = (Node) d;
			n.removeParent();
			n.setParent(this);
		}
	}


	public void remove(Drawable d)
	{
		this.child.remove(d);

		if(d instanceof Node)
		{
			((Node)d).removeParent();
		}
	}


	public void clearChild()
	{
		for(Drawable d : this.child)
		{
			if(d instanceof Node)
			{
				this.remove((Node)d);
				continue;
			}
			this.remove(d);
		}
	}




	public void forEachChild(ChildAction action)
	{
		this.forEachChild(1, action);
	}

	public void forEachChild(int nest, ChildAction action)
	{
		for(Drawable d : this.child)
		{
			action.action(nest, d);

			if(d instanceof Node)
			{
				Node node = (Node) d;
				node.forEachChild(nest, action);
				continue;
			}
		}

	}


	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String title = this.getClass().getSimpleName();

		sb.append(tab1);
		sb.append(title);
		sb.append("[").append(this.getName()).append("]");
		sb.append(enter);

		String tab2 = tab1 + "\t";

		for(Drawable d : this.child)
		{
			if(d instanceof TextTree)
			{
				TextTree t = (TextTree) d;
				sb = t.createTreeText(sb, nest + 1);
				continue;
			}

			sb.append(tab2);
			sb.append(d);
			sb.append(enter);
		}

		return sb;
	}



	@Override
	public void draw(Graphics2D g2)
	{
		AffineTransform old = g2.getTransform();
		g2.transform(this.transform);

		this.drawChild(g2);

		g2.setTransform(old);
	}

	protected void drawChild(Graphics2D g2)
	{
		for(Drawable d : this.child)
		{
			d.draw(g2);
		}
	}



	@Override
	public String toString()
	{
		return this.getName() + ": ";
	}

	@FunctionalInterface
	public static interface ChildAction {

		public void action(int nest, Drawable d);
	}


}
