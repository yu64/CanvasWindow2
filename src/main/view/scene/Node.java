package main.view.scene;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import main.debug.TextTree;

public class Node implements Drawable, TextTree{

	private Node parent = null;
	private Set<Drawable> child;
	private String name;
	private AffineTransform transform = new AffineTransform();

	public Node()
	{
		this(new LinkedHashSet<>(), "node");
	}

	public Node(String name)
	{
		this(new LinkedHashSet<>(), name);
	}

	public Node(Set<Drawable> childSet, String name)
	{
		this.child = childSet;
		this.name = name;
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




	public void add(Drawable d)
	{
		Objects.requireNonNull(d);
		this.child.add(d);
	}

	public void add(Node childNode)
	{
		this.add((Drawable)childNode);

		childNode.removeParent();
		childNode.setParent(this);
	}

	public void remove(Drawable d)
	{
		this.child.remove(d);
	}

	public void remove(Node childNode)
	{
		this.remove((Drawable)childNode);
		childNode.removeParent();

	}

	public void clearChild()
	{
		this.child.clear();
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
			if(d instanceof Node)
			{
				Node node = (Node) d;
				sb = node.createTreeText(sb, nest + 1);
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

		this.drawExtend(g2, v -> {
			
			for(Drawable d : this.child)
			{
				d.draw(v);
			}
			
		});

		g2.setTransform(old);
	}
	
	protected void drawExtend(Graphics2D g2, Consumer<Graphics2D> action)
	{
		action.accept(g2);
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
