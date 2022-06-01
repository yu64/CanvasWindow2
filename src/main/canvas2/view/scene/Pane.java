package canvas2.view.scene;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import canvas2.core.Drawable;
import canvas2.util.CastUtil;

/**
 * 指定の範囲に描画するクラス。<br>
 * ノード配下に登録すること。<br>
 * 外側と内側に分かれている<br>
 * スクロールなどを行う場合、このクラスを使用し、内側に適用することを推奨する。<br>
 */
public class Pane extends Node {


	private Node inner;
	private Shape shape;
	private Drawable background;

	public Pane(String outer, String inner)
	{
		super(outer);

		this.inner = this.createNode(inner);
		this.initNodeLink();
	}

	protected Node createNode(String name)
	{
		return new Node(name);
	}

	protected void initNodeLink()
	{
		this.add(this.getInnerNode());
	}

	public void setBackground(Drawable d)
	{
		this.background = d;
	}

	/**
	 * エリアの内側のノードを取得。
	 */
	public Node getInnerNode()
	{
		return this.inner;
	}

	public AffineTransform getInnerTransfrom()
	{
		return this.getInnerNode().getTransform();
	}

	public void setShape(Shape shape)
	{
		this.shape = shape;
	}

	public Shape getShape()
	{
		return this.shape;
	}

	public <S extends Shape> S getShape(Class<S> clazz)
	{
		return CastUtil.cast(this.shape);
	}

	@Override
	protected void drawChild(Graphics2D g2)
	{
		Shape temp = null;
		if(this.shape != null)
		{
			temp = g2.getClip();
			g2.setClip(this.shape);
		}

		if(this.background != null)
		{
			this.background.draw(g2);
		}

		this.drawArea(g2);

		if(this.shape != null)
		{
			g2.setClip(temp);
		}
	}

	protected void drawArea(Graphics2D g2)
	{
		super.drawChild(g2);
	}


}
