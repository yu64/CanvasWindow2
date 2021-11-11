package canvas2.event.awt;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import canvas2.event.EventManager;
import canvas2.event.EventRelay;
import canvas2.view.AppWindow;

/**
 * {@link EventManager}にAWTEventを紐づけるためのクラス。<br>
 * そのため、{@link AppWindow}を登録し、<br>
 * このクラスのオブジェクトを{@link EventManager}に登録する必要がある。
 *
 */
public class AwtRelay extends EventRelay implements AWTEventListener{

	private AppWindow window;

	public AwtRelay(AppWindow window)
	{
		this.window = window;
	}

	@Override
	public void eventDispatched(AWTEvent event)
	{
		if(this.isColsed())
		{
			return;
		}

		Object defaultSrc = event.getSource();


		if( !(defaultSrc instanceof Component))
		{
			return;
		}


		Component defaultComp = (Component) defaultSrc;
		Component src = defaultComp;

		if(event instanceof MouseEvent && defaultComp instanceof AppWindow)
		{
			KeyboardFocusManager m =  KeyboardFocusManager.getCurrentKeyboardFocusManager();
			src = m.getFocusOwner();
		}

		if(src == null)
		{
			src = defaultComp;
		}

		event.setSource(src);

		if( !(event instanceof MouseEvent))
		{
			this.getDestination().dispatch(event);
			return;
		}


		MouseEvent me = (MouseEvent) event;

		Point p1 = defaultComp.getLocationOnScreen();
		Point p2 = src.getLocationOnScreen();

		me.translatePoint(p1.x - p2.x, p1.y - p2.y);
		this.getDestination().dispatch(event);
	}

	@Override
	protected void registerSource()
	{
		this.window.getToolkit().addAWTEventListener(this, Long.MAX_VALUE);
	}

	@Override
	protected void unregisterSource()
	{
		this.window.getToolkit().removeAWTEventListener(this);
	}

}
