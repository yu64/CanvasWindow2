package canvas2.view.scene;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import canvas2.core.Drawable;
import canvas2.core.debug.TextTree;
import canvas2.util.TransformUtil;
import canvas2.view.AppWindow;
import canvas2.view.JScreen;

/**
 * シーングラフのノードを示すクラス。<br>
 * このノードを描画するときは、
 * {@link AppWindow}や{@link JScreen}のルートノードに登録する必要がある。<br>
 * このノードの配下で描画するときは、このノードに登録する必要がある。
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

	/**
	 * このNodeのTransformを取得します。<br>
	 * これを変更することで、スクロールや拡大を行えます。
	 */
	public AffineTransform getTransform()
	{
		return this.transform;
	}

	/**
	 * このノードの親を取得します。<br>
	 * 親が存在しない場合、nullを取得します。
	 */
	public Node getParent()
	{
		return this.parent;
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




	/**
	 * このNodeから親の一覧を取得する。<br>
	 * 一覧の先頭には、このNodeが設定されている。<br>
	 * その下が、このNodeの親である<br>
	 * 親がnullであるとき、その時点で一覧を作成を停止する。
	 */
	public <C extends Collection<Node>> C getParentList(C output)
	{
		Node now = this;
		while(now != null)
		{
			output.add(now);
			now = now.getParent();
		}

		return output;
	}

	/**
	 * すべての親を考慮し、{@link TransformUtil#inverseTransform}を実行します。<br>
	 * マウスの座標から、Nodeの座標を求める時に使用できます。
	 */
	public Point2D inverseTransform(Point2D in, Point2D out, List<Node> temp)
	{
		Point2D tempPoint = in;
		temp.clear();
		temp = this.getParentList(temp);

		for(int i = temp.size() - 1; 0 <= i; i--)
		{
			Node n = temp.get(i);
			AffineTransform t = n.getTransform();
			tempPoint = TransformUtil.inverseTransform(t, tempPoint, out);
		}

		temp.clear();

		return tempPoint;
	}

	/**
	 * すべての親を考慮し、{@link AffineTransform#transform}を実行します。
	 */
	public Point2D transform(Point2D in, Point2D out, List<Node> temp)
	{
		Point2D tempPoint = in;
		temp.clear();
		temp = this.getParentList(temp);

		for(int i = 0; i < temp.size(); i++)
		{
			Node n = temp.get(i);
			AffineTransform t = n.getTransform();
			tempPoint = t.transform(tempPoint, out);
		}

		temp.clear();

		return tempPoint;
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

		sb.append(tab1);
		sb.append(this.toString());
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

		synchronized (this.transform)
		{
			g2.transform(this.transform);
		}

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
		String name = this.getClass().getSimpleName();
		return name + "[" + this.getName() + "]";
	}

	/**
	 * 子に対して行う処理を示すクラス<br>
	 * メソッド<br>
	 * {@link ChildAction#action}
	 */
	@FunctionalInterface
	public static interface ChildAction {

		public void action(int nest, Drawable d);
	}

}
