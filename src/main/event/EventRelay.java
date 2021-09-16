package main.event;

import java.util.Objects;

public abstract class EventRelay {
	
	
	private EventManager event;

	final void init(EventManager event)
	{
		Objects.requireNonNull(event);
		this.event = event;
		
		this.registerSource();
	}
	
	final void close()
	{
		this.unregisterSource();
		this.event = null;
	}
	
	protected final EventManager getDestination()
	{
		return this.event;
	}
	
	protected final boolean isColsed()
	{
		return this.event == null;
	}
	
	protected abstract void registerSource();
	protected abstract void unregisterSource();
	
}
