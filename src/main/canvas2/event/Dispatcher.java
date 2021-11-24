package canvas2.event;

import java.util.EventObject;

import canvas2.core.event.Listener;

public interface Dispatcher<E extends EventObject> {


	public Class<E> getEventClass();

	public void addListener(Object exId, Listener<? super E> listener);
	public void removeListener(Object exId, Listener<? super E> listener);
	public void clearListener();

	public void dispatch(float tpf, E event);

	public StringBuilder createTreeText(StringBuilder sb, int nest);

}
