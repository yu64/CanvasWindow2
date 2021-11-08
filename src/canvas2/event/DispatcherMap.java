package canvas2.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import canvas2.debug.TextTree;
import canvas2.util.CastUtil;

public class DispatcherMap implements TextTree{


	private Map<Class<?>, Dispatcher<?, ?>> eventBase = new HashMap<>();
	private Map<Class<?>, Dispatcher<?, ?>> listenerBase = new HashMap<>();
	private Map<Class<?>, Dispatcher<?, ?>> classBase = new HashMap<>();


	public boolean addDispatcher(Dispatcher<?, ?> d)
	{
		if(this.eventBase.containsKey(d.getEventClass()))
		{
			return false;
		}

		if(this.listenerBase.containsKey(d.getListenerClass()))
		{
			return false;
		}
		
		if(this.classBase.containsKey(d.getClass()))
		{
			return false;
		}

		this.eventBase.put(d.getEventClass(), d);
		this.listenerBase.put(d.getListenerClass(), d);
		this.classBase.put(d.getClass(), d);

		return true;
	}

	public void removeDispatcher(Dispatcher<?, ?> d)
	{
		this.eventBase.remove(d.getEventClass(), d);
		this.listenerBase.remove(d.getListenerClass(), d);
		this.classBase.remove(d.getClass(), d);
	}

	public void removeAllDispatcher()
	{
		this.eventBase.clear();
		this.listenerBase.clear();
		this.classBase.clear();
	}
	
	public <D extends Dispatcher<?, ?>> D get(Class<D> clazz)
	{
		Dispatcher<?, ?> d = this.classBase.get(clazz);

		return CastUtil.cast(d);
	}

	public <E extends EventObject> void forEachEvent(Class<E> clazz, Consumer<Dispatcher<E, ?>> action)
	{
		for(Map.Entry<Class<?>, Dispatcher<?, ?>> e : this.eventBase.entrySet())
		{
			if(!e.getKey().isAssignableFrom(clazz))
			{
				continue;
			}

			Dispatcher<E, ?> d = CastUtil.cast(e.getValue());
			action.accept(d);
		}
	}

	public <L extends EventListener> void forEachListener(Class<L> clazz, Consumer<Dispatcher<?, L>> action)
	{
		for(Map.Entry<Class<?>, Dispatcher<?, ?>> e : this.listenerBase.entrySet())
		{
			if(!e.getKey().isAssignableFrom(clazz))
			{
				continue;
			}

			Dispatcher<?, L> d = CastUtil.cast(e.getValue());
			action.accept(d);
		}
	}

	public void forEach(Consumer<Dispatcher<?, ?>> action)
	{
		for(Dispatcher<?, ?> d : this.classBase.values())
		{
			action.accept(d);
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String title = this.getClass().getSimpleName();
		
		sb.append(tab1).append(title).append(enter);
		for(Dispatcher<?, ?> d : this.classBase.values())
		{
			sb = d.createTreeText(sb, nest + 1);
		}
		
		return sb;
	}
	

}
