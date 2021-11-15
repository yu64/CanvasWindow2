package canvas2.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * オブジェクトプーリングの機能を提供する。
 *
 */
public class Pool {

	public Map<Class<?>, PoolEntry<?>> entrys;



	public Pool()
	{
		this.entrys = this.createEntryMap();
	}

	/**
	 * プーリングするクラスを登録する。
	 *
	 * @param clazz 対象のクラス<br>
	 * @param f オブジェクトの生成方法<br>
	 * @param r 初期化方法<br>
	 * @param margin 初期プーリング量<br>
	 * @param limit プールの最大容量<br>
	 */
	public <T> void register(Class<T> clazz, Supplier<T> f, Consumer<T> r, int margin, int limit)
	{
		PoolEntry<T> e = new PoolEntry<>(clazz, f, r, margin, limit);
		this.register(clazz, e);
	}

	public <T> void register(Class<T> clazz, PoolEntry<T> entry)
	{
		if(this.entrys.containsKey(clazz))
		{
			throw new RuntimeException("Entry is exist. " + clazz);
		}

		this.entrys.put(clazz, entry);
		this.clear(this.entrys, clazz);
	}

	/**
	 * プーリングされたオブジェクトを取得する。
	 */
	public <T> T obtain(Class<T> clazz)
	{
		PoolEntry<T> e = CastUtil.cast(this.entrys.get(clazz));
		if(e == null)
		{
			throw new RuntimeException("Entry not defined. " + clazz);
		}

		return e.obtain();
	}

	/**
	 * オブジェクトを解放する。
	 */
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

	/**
	 * closeするとオブジェクトを解放する{@link AutoCloseable}を作成する。
	 *
	 */
	public <T> AutoCloseable getReleaseCloseable(T obj)
	{
		return () -> this.free(obj);
	}

	public <T> PoolEntry<T> getEntry(Class<T> clazz)
	{
		return CastUtil.cast(this.entrys.get(clazz));
	}





	protected Map<Class<?>, PoolEntry<?>> createEntryMap()
	{
		return new HashMap<>();
	}


	protected <T> void clear(Map<Class<?>, ?> map, Class<?> clazz)
	{
		map.remove(clazz);
	}


	public static class PoolEntry<T> {

		private Class<T> clazz;
		private Supplier<T> factory;
		private Consumer<T> initializer;
		private Deque<T> unused;
		private Set<T> used;
		private int limit;

		protected PoolEntry(Class<T> clazz, Supplier<T> f, Consumer<T> init, int margin, int limit)
		{
			this.clazz = clazz;
			this.factory = f;
			this.initializer = init;
			this.limit = limit;

			this.unused = this.createDeque();
			this.used = this.createSet();

			for(int i = 0; i < margin; i++)
			{
				this.addMargin();
			}
		}

		protected <T> Deque<T> createDeque()
		{
			return new ArrayDeque<T>();
		}

		protected <T> Set<T> createSet()
		{
			return new HashSet<T>();
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
			String name = obj.getClass().getSimpleName();
			if(!this.used.contains(obj))
			{
				throw new RuntimeException("It is free. Not exist used flag. " + name);
			}

			if(this.unused.contains(obj))
			{
				throw new RuntimeException("It is free. Exist unused flag. " + name);
			}

			this.used.remove(obj);

			if(!this.isFull())
			{
				this.initializer.accept(obj);
				this.unused.addLast(obj);
			}
		}

		public String toStringUsed()
		{
			return this.used.toString();
		}

		public String toStringUnused()
		{
			return this.unused.toString();
		}


		public int getUsedSize()
		{
			return this.used.size();
		}

		public int getUnusedSize()
		{
			return this.unused.size();
		}

		public void clearUsed(Consumer<T> closeFun)
		{
			for(T obj : this.used)
			{
				closeFun.accept(obj);
			}

			this.used.clear();
		}

		public void clearUnused(Consumer<T> closeFun)
		{
			for(T obj : this.unused)
			{
				closeFun.accept(obj);
			}

			this.unused.clear();
		}

		public int getSize()
		{
			return this.getUsedSize() + this.getUnusedSize();
		}

		public int getLimit()
		{
			return this.limit;
		}

		public boolean isFull()
		{
			return this.limit <= this.getSize();
		}

		protected void addMargin()
		{
			if(this.isFull())
			{
				return;
			}

			T obj = this.factory.get();
			this.initializer.accept(obj);
			this.unused.addLast(obj);
		}

	}
}




