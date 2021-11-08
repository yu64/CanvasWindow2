package canvas2.view.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.function.Consumer;

import canvas2.view.AppWindow;

/**
 * 外側と内側の二つの{@link Node}が合わさったクラス。<br>
 * {@link AppWindow}のルートノード配下に登録すること。
 */
public class FrameNode extends Node{

	private Node innerNode;
	private Shape frameShape;
	private Color backColor = Color.WHITE;

	public FrameNode(String outerName, String innerName, int width, int height)
	{
		this(outerName, innerName);
		this.setFrameRect(width, height);
	}

	public FrameNode(String outerName, String innerName, Shape frameShape)
	{
		super(outerName);
		this.setFrameShape(frameShape);
	}

	public FrameNode(String outerName, String innerName)
	{
		super(outerName);
		this.innerNode = new Node(innerName);
		this.add(this.innerNode);
	}

	public Node getInnerNode()
	{
		return this.innerNode;
	}

	public void setFrameRect(int width, int height)
	{
		this.setFrameShape(new Rectangle(0, 0, width, height));
	}

	public void setFrameShape(Shape shape)
	{
		this.frameShape = shape;
	}

	public Shape getFrameShape()
	{
		return this.frameShape;
	}

	public void setBackgroundColor(Color back)
	{
		this.backColor = back;
	}

	@Override
	protected void drawExtend(Graphics2D g2, Consumer<Graphics2D> action)
	{
		Shape s = g2.getClip();
		g2.setClip(this.frameShape);

		g2.setColor(this.backColor);
		g2.fill(this.frameShape);

		action.accept(g2);

		g2.setClip(s);
	}



}
