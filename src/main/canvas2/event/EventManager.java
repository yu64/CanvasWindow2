package canvas2.event;

import java.util.ArrayDeque;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

import canvas2.core.Trigger;
import canvas2.core.Updatable;
import canvas2.debug.TextTree;
import canvas2.event.basic.BasicDispatcher;
import canvas2.util.CastUtil;

public class EventManager implements Updatable, TextTree{

	private Queue<EventObject> queue;
	private Set<Dispatcher<?>> dispatchers = new HashSet<>();


	public EventManager()
	{
		this.queue = this.createQueue();
	}


	protected Queue<EventObject> createQueue()
	{
		return new ArrayDeque<>();
	}

	protected <T> Set<T> createSet()
	{
		return new HashSet<>();
	}


	public <E extends EventObject> void setDispatcher(Class<E> e, Dispatcher<E> in)
	{
		Iterator<Dispatcher<?>> ite = this.dispatchers.iterator();
		while(ite.hasNext())
		{
			Dispatcher<?> d = ite.next();
			if(!d.getEventClass().isAssignableFrom(e))
			{
				continue;
			}

			ite.remove();
		}

		this.dispatchers.add(in);
	}




	public <E extends EventObject> void add(Class<E> e, Listener<E> listener)
	{
		this.add(e, null, listener);
	}

	public <E extends EventObject> void add(Class<E> e, Object exId, Listener<E> listener)
	{
		boolean isAdded = false;
		for(Dispatcher<?> d : this.dispatchers)
		{
			if(!d.getEventClass().isAssignableFrom(e))
			{
				continue;
			}

			Dispatcher<E> d2 = CastUtil.cast(d);
			d2.addListener(exId, listener);
			isAdded = true;
		}

		if(isAdded)
		{
			return;
		}

		Dispatcher<E> d = new BasicDispatcher<E>(e);
		d.addListener(exId, listener);

		this.dispatchers.add(d);
	}



	public <E extends EventObject> void remove(Class<E> e, Listener<E> listener)
	{
		this.add(e, null, listener);
	}

	public <E extends EventObject> void remove(Class<E> e, Object exId, Listener<E> listener)
	{
		Iterator<Dispatcher<?>> ite = this.dispatchers.iterator();
		while(ite.hasNext())
		{
			Dispatcher<?> d = ite.next();
			if(!d.getEventClass().isAssignableFrom(e))
			{
				continue;
			}

			Dispatcher<E> d2 = CastUtil.cast(d);
			d2.removeListener(exId, listener);
		}

	}


	public void clear()
	{
		this.dispatchers.clear();
	}


	public void dispatch(EventObject e)
	{
		this.queue.add(e);
	}

	@Override
	public void update(float tpf) throws Exception
	{
		while(!this.queue.isEmpty())
		{
			EventObject e = this.queue.poll();
			this.invokeListener(tpf, e);
		}
	}

	public <E extends EventObject> void invokeListener(float tpf, E e)
	{
		Class<?> clazz = e.getClass();
		for(Dispatcher<?> d : this.dispatchers)
		{
			if(!d.getEventClass().isAssignableFrom(clazz))
			{
				continue;
			}

			Dispatcher<E> d2 = CastUtil.cast(d);
			d2.dispatch(tpf, e);
		}
	}



	public Trigger createTrigger()
	{
		return new TriggerImpl();
	}

	protected class TriggerImpl implements Trigger
	{

		protected TriggerImpl()
		{

		}

		public void dispatch(EventObject o)
		{
			EventManager.this.dispatch(o);
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab0 = "\t".repeat(nest);

		String title = this.getClass().getSimpleName();

		sb.append(tab0);
		sb.append(title);
		sb.append(enter);

		for(Dispatcher<?> d : this.dispatchers)
		{
			d.createTreeText(sb, nest + 1);
		}


		return sb;
	}



}
