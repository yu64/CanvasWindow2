package canvas2.event;

import java.util.EventObject;

public interface Dispatcher<E extends EventObject> {


	public Class<E> getEventClass();

	public void addListener(Object exId, Listener<E> listener);
	public void removeListener(Object exId, Listener<E> listener);
	public void clearListener();

	public void dispatch(float tpf, E event);

	public StringBuilder createTreeText(StringBuilder sb, int nest);

}
