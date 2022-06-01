package canvas2.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 同じ型の複数のキーをもとにしたマップ。
 * {@link Collection}を継承していない。
 */
public class MultiKeyMap<K, V> {


	private int dim;
	private Map<Keys<K>, V> map;

	public MultiKeyMap(int dim)
	{
		if(dim < 1)
		{
			throw new RuntimeException("Key dimension is (1 < dim)");
		}
		this.map = this.createMap();
		this.dim = dim;
	}

	protected <A, B> Map<A, B> createMap()
	{
		return new HashMap<>();
	}

	protected Keys<K> createKeysObj(K[] keys)
	{
		return new Keys<K>(keys);
	}

	/**
	 * 指定のキーが有効であるか。
	 * 有効でない場合、例外を投げる。
	 */
	protected void requireValidKey(K[] keys)
	{
		Objects.requireNonNull(keys);

		int dim = this.dim;
		if(keys.length != dim)
		{
			throw new RuntimeException("Key is too shot or long. Required: " + dim);
		}
	}


	/**
	 * 複数のキーで値を設定します。
	 */
	@SafeVarargs
	public final V put(V value, K... keys)
	{
		this.requireValidKey(keys);
		Keys<K> obj = this.createKeysObj(keys);
		return this.map.put(obj, value);
	}

	/**
	 * 複数のキーに対応した値を削除します。
	 */
	@SafeVarargs
	public final V remove(K... keys)
	{
		this.requireValidKey(keys);
		Keys<K> obj = this.createKeysObj(keys);
		return this.map.remove(obj);
	}

	/**
	 * 複数のキーに対応した値を取得します。
	 */
	@SafeVarargs
	public final V get(K... keys)
	{
		this.requireValidKey(keys);
		Keys<K> obj = this.createKeysObj(keys);

		return this.map.get(obj);
	}

	/**
	 * 複数のキーに対応した値を取得します。
	 * 存在しない場合、代替の値を使用できる。
	 */
	@SafeVarargs
	public final V getIfPresent(V defaultValue, K... keys)
	{
		V o = this.get(keys);
		return (o != null ? o : defaultValue);
	}

	/**
	 * 複数のキーに対応した値を取得します。
	 * 存在しない場合、代替の値を使用できる。
	 */
	@SafeVarargs
	public final V getIfPresent(Supplier<V> defaultValue, K... keys)
	{
		V o = this.get(keys);
		return (o != null ? o : defaultValue.get());
	}

	/**
	 * すべて削除
	 */
	public void clear()
	{
		this.map.clear();
	}


	/**
	 * {@link Map.Entry}を{@link Iterable}で取得します。
	 */
	public Iterable<Entry<Keys<K>, V>> iterable()
	{
		return this.map.entrySet();
	}

	public Iterable<V> getValues()
	{
		return this.map.values();
	}

	public Iterable<Keys<K>> getKeys()
	{
		return this.map.keySet();
	}


	public static class Keys<K> implements Iterable<K>{

		private K[] keys;

		@SafeVarargs
		protected Keys(K... keys)
		{
			this.keys = keys;
		}

		public int getDimension()
		{
			return this.keys.length;
		}

		public K getKey(int index)
		{
			return this.keys[index];
		}

		@Override
		public Iterator<K> iterator()
		{
			return Arrays.asList(this.keys).iterator();
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o)
			{
				return true;
			}

			if(o instanceof Keys)
			{
				Keys<?> keys = (Keys<?>) o;
				return Arrays.equals(this.keys, keys.keys);
			}

			return true;
		}


		@Override
		public int hashCode()
		{
			return Arrays.hashCode(this.keys);
		}



	}





}
