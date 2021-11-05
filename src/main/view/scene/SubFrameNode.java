package main.view.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

import main.util.TransformUtil;
import main.view.AppWindow;

/**
 * 指定の{@link Node}を指定の範囲に描画するクラス。<br>
 * {@link AppWindow}のルートノード配下に登録すること。
 */
public class SubFrameNode extends Node{

	private Node targetNode;
	private Node innerNode;
	private Shape shape;
	private Color back = Color.LIGHT_GRAY;


	public SubFrameNode(String name, Node targetNode)
	{
		super(name);
		this.targetNode = targetNode;
		this.innerNode = new Node(name + "_inner");


		this.add(this.innerNode);


		this.innerNode.add(g2 -> {

			this.targetNode.draw(g2);
		});

	}

	/**
	 * 描画範囲を矩形の範囲を指定します。<br>
	 * 原点を中心に、指定の縦幅、横幅の矩形です。
	 */
	public void setRect(int width, int height)
	{
		double x = -width / 2.0D;
		double y = -height / 2.0D;

		Rectangle rect = new Rectangle();

		rect.setFrame(x, y, width, height);
		this.shape = rect;
	}

	public void setShape(Shape shape)
	{
		this.shape = shape;
	}

	public void setBackgroundColor(Color back)
	{
		this.back = back;
	}

	/**
	 * 指定座標を中心にします。
	 */
	public void track(double x, double y)
	{
		AffineTransform t = this.innerNode.getTransform();
		TransformUtil.setTranslate(t, -x, -y);
	}

	/**
	 * 指定座標を中心にします。
	 */
	public void track(Point pos)
	{
		this.track(pos.getX(), pos.getY());
	}

	/**
	 * このオブジェクトと、描画対象の{@link Node}、<br>
	 * の間にある{@link Node}を取得。<br>
	 * 装飾をつけるときに使用できる。
	 */
	public Node getInnerNode()
	{
		return this.innerNode;
	}

	@Override
	protected void drawExtend(Graphics2D g2, Consumer<Graphics2D> action)
	{
		Shape s = g2.getClip();
		g2.setClip(this.shape);

		g2.setColor(this.back);
		g2.fill(this.shape);

		action.accept(g2);

		g2.setClip(s);
	}





}
