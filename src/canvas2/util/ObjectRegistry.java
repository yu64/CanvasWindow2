package canvas2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 複数のインターフェイスを持つクラスをそれぞれ登録するコレクション。<br>
 * 標準のコレクションとは互換性がない。<br>
 *
 */
@SuppressWarnings("unchecked")
public class ObjectRegistry<T> {

	//すべてのオブジェクトを管理するマップ
	private final Map<Class<?>, Collection<?>> pair = new ConcurrentHashMap<>();


	//管理するオブジェクトの種類を追加
	public void registerClass(Class<? extends T> clazz, Collection<? extends T> c)
	{
		this.pair.put(clazz, c);
	}

	//管理するオブジェクトの種類を追加
	public void registerClass(Class<? extends T> clazz)
	{
		this.registerClass(clazz, new ArrayList<T>());
	}

	//管理するオブジェクトの種類を削除。
	public void unregisterClass(Class<? extends T> clazz)
	{
		this.pair.remove(clazz);
	}





	public <O extends T> boolean add(O obj)
	{
		boolean output = false;
		for(Entry<Class<?>, Collection<?>> e : this.pair.entrySet())
		{
			if( !(e.getKey().isAssignableFrom(obj.getClass())))
			{
				continue;
			}

			Collection<O> c = (Collection<O>) e.getValue();
			if(c.add(obj))
			{
				output = true;
			}
		}


		return output;
	}

	public <O extends T> Collection<O> get(Class<O> clazz)
	{
		return (Collection<O>) this.pair.get(clazz);
	}


	public <O extends T> boolean remove(O obj)
	{
		boolean output = false;
		for(Entry<Class<?>, Collection<?>> e : this.pair.entrySet())
		{
			if( !(e.getKey().isAssignableFrom(obj.getClass())))
			{
				continue;
			}

			Collection<O> c = (Collection<O>) e.getValue();
			if(c.remove(obj))
			{
				output = true;
			}
		}

		return output;
	}

	public void removeAll()
	{
		for(Entry<Class<?>, Collection<?>> e : this.pair.entrySet())
		{
			e.getValue().clear();
		}
	}


}
