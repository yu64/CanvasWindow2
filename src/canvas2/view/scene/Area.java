package canvas2.view.scene;

import java.awt.Graphics2D;
import java.awt.Shape;

import canvas2.util.CastUtil;
import canvas2.view.AppWindow;

/**
 * 指定の範囲に描画するクラス。<br>
 * {@link AppWindow}のルートノード配下に登録すること。<br>
 * 外側と内側に分かれている。<br>
 */
public class Area extends Node {


	private Node inner;
	private Shape shape;
	private Drawable background;

	public Area(String outer, String inner)
	{
		super(outer);

		this.inner = this.createNode(inner);
		this.add(this.inner);
	}

	protected Node createNode(String name)
	{
		return new Node(name);
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
