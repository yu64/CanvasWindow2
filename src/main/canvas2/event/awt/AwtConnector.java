package canvas2.event.awt;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;

import canvas2.core.event.Trigger;
import canvas2.event.EventManager;

public class AwtConnector implements AWTEventListener{

	private Trigger trigger;

	public AwtConnector(EventManager manager)
	{
		this(manager.createTrigger());
	}

	public AwtConnector(Trigger trigger)
	{
		this.trigger = trigger;
	}

	@Override
	public void eventDispatched(AWTEvent event)
	{
		this.trigger.dispatch(event);
	}


}
