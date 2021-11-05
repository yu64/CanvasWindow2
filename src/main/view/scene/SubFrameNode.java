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
	private Rectangle rect = new Rectangle();

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

	public void setSize(int width, int height)
	{
		double x = -width / 2.0D;
		double y = -height / 2.0D;

		this.rect.setFrame(x, y, width, height);
	}

	public void track(double x, double y)
	{
		AffineTransform t = this.innerNode.getTransform();
		TransformUtil.setTranslate(t, -x, -y);
	}

	public void track(Point pos)
	{
		this.track(pos.getX(), pos.getY());
	}

	public Node getInnerNode()
	{
		return this.innerNode;
	}

	@Override
	protected void drawExtend(Graphics2D g2, Consumer<Graphics2D> action)
	{
		Shape s = g2.getClip();
		g2.setClip(this.rect);

		g2.setColor(Color.LIGHT_GRAY);
		g2.fill(this.rect);

		action.accept(g2);

		g2.setClip(s);
	}





}
