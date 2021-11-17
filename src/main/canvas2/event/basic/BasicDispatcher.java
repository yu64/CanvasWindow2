package canvas2.event.basic;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import canvas2.event.Dispatcher;
import canvas2.event.Listener;

public class BasicDispatcher<E extends EventObject> implements Dispatcher<E>{


	private Class<E> event;
	private Set<Listener<E>> listeners;

	public BasicDispatcher(Class<E> event)
	{
		this.event = event;
		this.listeners = this.createSet();
	}

	protected <T> Set<T> createSet()
	{
		return new HashSet<>();
	}

	protected Set<Listener<E>> getListeners()
	{
		return this.listeners;
	}

	@Override
	public Class<E> getEventClass()
	{
		return this.event;
	}

	@Override
	public void addListener(Object exId, Listener<E> listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(Object exId, Listener<E> listener)
	{
		this.listeners.remove(listener);
	}

	@Override
	public void clearListener()
	{
		this.listeners.clear();
	}

	@Override
	public void dispatch(float tpf, E event)
	{
		for(Listener<E> h : this.listeners)
		{
			h.actAndThrow(tpf, event);
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String tab2 = tab1 + "\t";
		String title = this.getClass().getSimpleName();

		sb.append(tab1).append(title).append(enter);

		for(Listener<E> h : this.listeners)
		{
			sb.append(tab2).append(h).append(enter);
		}

		return sb;
	}

}
