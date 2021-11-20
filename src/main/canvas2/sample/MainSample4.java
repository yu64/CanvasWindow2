package canvas2.sample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JSplitPane;

import canvas2.App;
import canvas2.view.AppWindow;
import canvas2.view.JScreen;
import canvas2.view.scene.Node;
import canvas2.view.swing.JButtonScreen;

public class MainSample4 {


	public static void main(String[] args)
	{
		App app = new App();

		MainSample2.testColsed(app);

		Node leftRoot = new Node("leftRoot");
		Node rightRoot = new Node("rightRoot");

		MainSample2.testAxis(app, leftRoot);
		MainSample2.testAxis(app, rightRoot);

		MainSample2.testMouse(app, leftRoot, leftRoot);
		MainSample2.testMouse(app, rightRoot, rightRoot);


		AppWindow win = app.getWindow();
		JScreen screen = win.getScreen();


		JScreen leftScreen = new JScreen(leftRoot);
		JScreen rightScreen = new JScreen(rightRoot);


		JSplitPane split = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				leftScreen,
				rightScreen
				);


		JButton button = new JButton("Test Button");
		leftScreen.add(button, BorderLayout.NORTH);
		
		JButtonScreen button2 = new JButtonScreen("Test Button 2");
		button2.setBackground(g2 -> {
			
			Dimension d = button2.getSize();
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(0, 0, d.width, d.height);
		});
		
		button2.setHover(g2 -> {
			
			Dimension d = button2.getSize();
			g2.setColor(Color.CYAN);
			g2.fillRect(0, 0, d.width, d.height);
			
		});
		
		button2.setClicked(g2 -> {
			
			
			Dimension d = button2.getSize();
			g2.setColor(Color.GREEN);
			g2.fillRect(0, 0, d.width, d.height);
		});
		
		leftScreen.add(button2, BorderLayout.SOUTH);
		
		screen.add(split);



		app.start();
	}





}
