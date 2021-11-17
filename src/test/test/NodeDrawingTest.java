package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import canvas2.core.Drawable;
import canvas2.view.scene.Node;

class NodeDrawingTest {

	private Node root;
	private Node groupA;
	private Node groupB;
	private Drawable item1;
	private Drawable item2;
	private Drawable item3;

	private BufferedImage image;

	@BeforeEach
	public void setup()
	{
		System.out.println(this.getClass().getSimpleName() + " setup");

		this.root = new Node("root");
		this.groupA = new Node("groupA");
		this.groupB = new Node("groupB");

		this.item1 = (g2 -> {

			g2.setColor(Color.GREEN);
			g2.fillRect(0, 0, 100, 100);
			System.out.println("item1");
		});

		this.item2 = (g2 -> {

			g2.setColor(Color.RED);
			g2.fillOval(0, 0, 10, 10);
			System.out.println("item2");
		});

		this.item3 = (g2 -> {

			g2.setColor(Color.BLUE);
			g2.drawOval(0, 0, 100, 100);
			System.out.println("item3");
		});

		this.image = new BufferedImage(1000, 1000, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) this.image.getGraphics();

		this.item1.draw(g2);
		this.item2.draw(g2);
		this.item3.draw(g2);

		g2.dispose();
	}


	@Test
	void test()
	{
		System.out.println(this.getClass().getSimpleName());

		this.root.add(this.item1);
		this.root.add(this.groupA);

		this.groupA.add(this.item2);
		this.groupA.add(this.groupB);

		this.groupB.add(this.item3);

		BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) image.getGraphics();

		this.root.draw(g2);
		g2.dispose();

		boolean output = NodeDrawingTest.equalImage(image, this.image);

		if(!output)
		{
			new JImageFrame(image, "now", false).setVisible(true);
			new JImageFrame(this.image, "target", true).setVisible(true);
		}


		Assertions.assertTrue(output);



	}

	public static boolean equalImage(BufferedImage imgA, BufferedImage imgB) {

		if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight())
		{
			return false;
		}

		int width = imgA.getWidth();
		int height = imgA.getHeight();

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (imgA.getRGB(x, y) != imgB.getRGB(x, y))
				{

					return false;
				}
			}
		}

		return true;
	}

}
