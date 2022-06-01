package canvas2.event.basic;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import canvas2.core.event.Listener;
import canvas2.event.Dispatcher;
import canvas2.event.EventManager;

/**
 * 標準のディスパッチャー
 * @sse {@link EventManager#setDispatcher(Class, Dispatcher)}
 */
public class BasicDispatcher<E extends EventObject> implements Dispatcher<E>{


	private Class<E> event;
	private Set<Listener<? super E>> listeners;

	public BasicDispatcher(Class<E> event)
	{
		this.event = event;
		this.listeners = this.createSet();
	}

	protected <T> Set<T> createSet()
	{
		return new HashSet<>();
	}

	public Set<Listener<? super E>> getListeners()
	{
		return this.listeners;
	}

	@Override
	public Class<E> getEventClass()
	{
		return this.event;
	}

	@Override
	public void addListener(Object exId, Listener<? super E> listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(Object exId, Listener<? super E> listener)
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
		for(Listener<? super E> h : this.listeners)
		{
			h.actOrThrow(tpf, event);
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

		for(Listener<? super E> h : this.listeners)
		{
			sb.append(tab2).append(h).append(enter);
		}

		return sb;
	}

}
