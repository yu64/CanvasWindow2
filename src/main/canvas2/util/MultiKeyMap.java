package canvas2.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;

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
	
	protected void requireValidKey(K[] keys)
	{
		Objects.requireNonNull(keys);
		
		int dim = this.dim;
		if(keys.length != dim)
		{
			throw new RuntimeException("Key is too shot or long. Required: " + dim);
		}
	}
	

	@SafeVarargs
	public final V put(V value, K... keys)
	{
		this.requireValidKey(keys);
		Keys<K> obj = this.createKeysObj(keys);
		return this.map.put(obj, value);
	}

	@SafeVarargs
	public final V remove(K... keys)
	{
		this.requireValidKey(keys);
		Keys<K> obj = this.createKeysObj(keys);
		return this.map.remove(obj);
	}

	@SafeVarargs
	public final V get(K... keys)
	{
		this.requireValidKey(keys);
		Keys<K> obj = this.createKeysObj(keys);
		
		return this.map.get(obj);
	}

	@SafeVarargs
	public final V getIfPresent(V defaultValue, K... keys)
	{
		V o = this.get(keys);
		return (o != null ? o : defaultValue);
	}

	@SafeVarargs
	public final V getIfPresent(Supplier<V> defaultValue, K... keys)
	{
		V o = this.get(keys);
		return (o != null ? o : defaultValue.get());
	}
	
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
