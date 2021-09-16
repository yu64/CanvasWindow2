package main.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings("unchecked")
public class ObjectRegistry<T> {

	//すべてのオブジェクトを管理するマップ
	private final Map<Class<? extends T>, Collection<? extends T>> pair = new ConcurrentHashMap<>();



	//管理するオブジェクトの種類を追加
	public void registerClass(Class<? extends T> clazz, Collection<? extends T> c)
	{
		this.pair.put(clazz, c);
	}

	//管理するオブジェクトの種類を削除。
	public void unregisterClass(Class<? extends T> clazz)
	{
		this.pair.remove(clazz);
	}


	public <O extends T> void register(O obj)
	{
		for(Entry<Class<? extends T>, Collection<? extends T>> e : this.pair.entrySet())
		{
			if( !(e.getKey().isAssignableFrom(obj.getClass())))
			{
				continue;
			}


			Collection<O> c = (Collection<O>) e.getValue();
			c.add(obj);
		}
	}

	public <O extends T> Collection<O> get(Class<O> clazz)
	{
		return (Collection<O>) this.pair.get(clazz);
	}


	public <O extends T> void unregister(O obj)
	{
		for(Entry<Class<? extends T>, Collection<? extends T>> e : this.pair.entrySet())
		{
			if( !(e.getKey().isAssignableFrom(obj.getClass())))
			{
				continue;
			}

			Collection<O> c = (Collection<O>) e.getValue();
			c.remove(obj);
		}
	}

	public void unregisterAll()
	{
		for(Entry<Class<? extends T>, Collection<? extends T>> e : this.pair.entrySet())
		{
			e.getValue().clear();
		}
	}


}
