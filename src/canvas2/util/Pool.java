package canvas2.util;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Pool {

	public Map<Class<?>, PoolEntry<?>> entrys;



	public Pool()
	{
		this.entrys = this.createEntryMap();
	}

	public <T> void register(Class<T> clazz, Supplier<T> f, Consumer<T> r, int initUnusedCount)
	{
		PoolEntry<T> e = new PoolEntry<>(clazz, f, r);
		this.entrys.put(clazz, e);

		for(int i = 0; i < initUnusedCount; i++)
		{
			e.addMargin();
		}
	}

	public <T> T obtain(Class<T> clazz)
	{
		PoolEntry<T> e = CastUtil.cast(this.entrys.get(clazz));
		if(e == null)
		{
			throw new RuntimeException("Entry not defined. " + clazz);
		}

		return e.obtain();
	}

	public <T> void free(T obj)
	{
		Class<T> clazz = CastUtil.getClass(obj);
		PoolEntry<T> e = CastUtil.cast(this.entrys.get(clazz));
		if(e == null)
		{
			throw new RuntimeException("Entry not defined. " + clazz);
		}

		e.free(obj);
	}

	public <T> AutoCloseable closeable(T obj)
	{
		return () -> this.free(obj);
	}

	public <T> PoolEntry<T> getEntry(Class<T> clazz)
	{
		return CastUtil.cast(this.entrys.get(clazz));
	}


	protected <T> Deque<T> createDeque()
	{
		return new ArrayDeque<T>();
	}

	protected <T> Set<T> createSet()
	{
		return new HashSet<T>();
	}

	protected Map<Class<?>, PoolEntry<?>> createEntryMap()
	{
		return new HashMap<>();
	}


	protected <T, C extends Collection<?>> void clear(Map<Class<?>, C> map, Class<?> clazz)
	{
		map.remove(clazz);
	}


	public class PoolEntry<T> {

		private Class<T> clazz;
		private Supplier<T> factory;
		private Consumer<T> initializer;
		private Deque<T> unused;
		private Set<T> used;

		protected PoolEntry(Class<T> clazz, Supplier<T> f, Consumer<T> init)
		{
			this.clazz = clazz;
			this.factory = f;
			this.initializer = init;

			this.unused = Pool.this.createDeque();
			this.used = Pool.this.createSet();
		}

		public Class<T> getType()
		{
			return this.clazz;
		}

		public T obtain()
		{
			T obj = this.unused.pollFirst();
			if(obj == null)
			{
				obj = this.factory.get();
				this.initializer.accept(obj);
			}

			this.used.add(obj);

			return obj;
		}

		public void free(T obj)
		{
			if(!this.used.contains(obj))
			{
				throw new RuntimeException("It is free." + obj);
			}

			if(this.unused.contains(obj))
			{
				throw new RuntimeException("It is free." + obj);
			}

			this.used.remove(obj);
			this.unused.addLast(obj);
		}

		protected void addMargin()
		{
			T obj = this.factory.get();
			this.initializer.accept(obj);
			this.unused.addLast(obj);
		}

	}
}




