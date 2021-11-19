package canvas2.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InterveneMap<K> {

	private Map<K, Entry> data = this.createMap();
	private boolean canThrow;
	
	public InterveneMap(boolean canThrow)
	{
		this.canThrow = canThrow;
	}
	
	protected Map<K, Entry> createMap()
	{
		return new HashMap<>();
	}
	
	protected Entry createEntry(Class<?> type)
	{
		return new Entry(type);
	}
	
	
	/**
	 * キーを登録する。
	 */
	public void register(K key, Class<?> type)
	{
		Entry e = this.data.get(key);
		if(e == null)
		{
			e = this.createEntry(type);
			this.data.put(key, e);
			return;
		}
		
		if(this.canThrow)
		{
			throw new RuntimeException("Already registered. Key: " + key);
		}
		
	}
	
	/**
	 * 指定されたキーに対応した値を設定する。<br>
	 * キーが登録されていない場合、設定できない。
	 */
	public boolean set(K key, Object value)
	{
		Entry e = this.data.get(key);
		if(e == null)
		{
			if(this.canThrow)
			{
				new RuntimeException("Not register. Key: " + key);
			}
			return false;
		}
		
		
		if(!e.getType().isAssignableFrom(value.getClass()))
		{
			if(this.canThrow)
			{
				new RuntimeException("Not apply class. Required: " + e.getType());
			}
			
			return false;
		}
		
		Object prev = e.getValue();
		e.setValue(value);
		this.data.put(key, e);
		this.fireChange(key, prev, value);
		
		return true;
	}
	


	
	
	/**
	 * 指定したキーの値を取得する。<br>
	 * キーが登録されていない場合、例外を投げることができる。<br>
	 * 値がnullであるとき、代わりの値を返す。<br>
	 */
	public Object get(K key)
	{
		Entry e = this.data.get(key);
		if(e == null)
		{
			if(this.canThrow)
			{
				new RuntimeException("Not register. Key: " + key);
			}
			
			return null;
		}
		
		Object o = e.getValue();
		if(o == null)
		{
			return null;
		}
		
		return o;
	}
	
	public Object get(K key, Supplier<Object> def)
	{
		
	}
	
	
	
	
	public void remove(K key)
	{
		Object prev = this.get(key);
		this.data.remove(key);
		
		this.fireChange(key, prev, null);
	}
	
	public void setNull(K key)
	{
		this.set(key, null);
	}
	
	protected void fireChange(K key, Object prev, Object next)
	{
		Entry e = this.data.get(key);
		if(e == null || e.getListener() == null)
		{
			return;
		}
		
		e.getListener().onChange(this, key, prev, next);
	}
	
	
	
	
	
	protected class Entry {
	
		private Class<?> type;
		private Object value;
		private ChangeListener<K> listener;
		
		protected Entry(Class<?> type)
		{
			this.type = type;
		}
	
		public Class<?> getType()
		{
			return this.type;
		}

		public Object getValue()
		{
			return this.value;
		}
		
		protected void setValue(Object value)
		{
			this.value = value;
		}

		public ChangeListener<K> getListener()
		{
			return listener;
		}

		protected void setListener(ChangeListener<K> listener)
		{
			this.listener = listener;
		}
		
	}
	
	
	public interface ChangeListener<K> {
		
		public void onChange(InterveneMap<K> src, K key, Object prev, Object next);
	}
	
	
	
	
	
	
	
}
