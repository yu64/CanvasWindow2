package canvas2.sample;

import java.awt.BorderLayout;

import javax.swing.JButton;

import canvas2.App;
import canvas2.logic.AppLogic;
import canvas2.view.AppWindow;
import canvas2.view.JScreen;
import canvas2.view.JTransformPanel;
import canvas2.view.scene.Area;
import canvas2.view.scene.Node;

public class MainSample3 {


	public static void main(String[] args)
	{
		App app = new App();


		Area area = new Area("areaNode", "areaInner");
		app.getRootNode().add(area);

		Node overray = new Node("overrayNode");
		app.getRootNode().add(overray);

		MainSample1.testAxis(app, area.getInnerNode());
		MainSample1.testZoom(app, area.getInnerNode());
		MainSample1.testMove(app, area.getInnerNode());

		AppLogic logic = app.getLogic();
		AppWindow win = app.getWindow();
		JScreen screen = win.getScreen();

		screen.setLayout(new BorderLayout());



		JTransformPanel panel = new JTransformPanel(area.getInnerNode().getTransform());
		logic.add(panel);
		screen.add(panel);

		JButton button = new JButton("Test");
		button.setBounds(0, 0, 100, 100);
		panel.getInnerPanel().add(button);


		area.getInnerNode().add(g2 -> button.paint(g2));

		app.start();
	}
}
