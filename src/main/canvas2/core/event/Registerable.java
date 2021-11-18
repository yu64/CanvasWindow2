package canvas2.core.event;

import canvas2.event.EventManager;

public interface Registerable {

	public void registerTo(EventManager event);
	public void unregisterTo(EventManager event);
}
