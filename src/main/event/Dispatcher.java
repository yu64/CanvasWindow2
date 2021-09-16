package main.event;

import java.util.EventListener;
import java.util.EventObject;

import main.debug.TextTree;

public interface Dispatcher<E extends EventObject, L extends EventListener> extends TextTree{


	public Class<E> getEventClass();
	public Class<L> getListenerClass();

	public void addListener(Object exId, L listener);
	public void removeListener(Object exId, L listener);
	public void clearListener();

	public void dispatch(float tpf, E event);

	public StringBuilder createTreeText(StringBuilder sb, int nest);
}
