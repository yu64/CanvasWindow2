package canvas2.event;

public interface Registerable {

	public void registerTo(EventManager event);
	public void unregisterTo(EventManager event);
}
