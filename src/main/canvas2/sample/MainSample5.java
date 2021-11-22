package canvas2.sample;

import java.awt.Color;
import java.awt.Dimension;

import canvas2.App;
import canvas2.view.scene.Node;

public class MainSample5 {


	public static void main(String[] args)
	{


	}

	public static void testRect(App app, Node node, Dimension size)
	{
		int h = size.height;
		int w = size.width;

		node.add(g2 -> {

			g2.setColor(Color.RED);
			g2.drawRect(0, 0, w, h);
			g2.drawLine(0, 0, w, h);
			g2.drawLine(0, h, w, 0);
		});

	}




}
