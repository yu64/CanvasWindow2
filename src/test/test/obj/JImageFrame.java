package test.obj;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JImageFrame extends JDialog{

	public JImageFrame(Image image, String title, boolean modal)
	{
		super((JFrame)null, title, modal);

		JPanel p = new JPanel() {

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
			}

		};

		Dimension s = new Dimension(image.getWidth(null), image.getHeight(null));
		p.setPreferredSize(s);

		this.add(p);
		this.pack();

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e)
			{
				JImageFrame.this.dispose();
			}
		});

	}



}
