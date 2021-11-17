package canvas2.sample;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JSplitPane;

import canvas2.App;
import canvas2.view.AppWindow;
import canvas2.view.JScreen;
import canvas2.view.scene.Node;

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

		screen.add(split);



		app.start();
	}





}
