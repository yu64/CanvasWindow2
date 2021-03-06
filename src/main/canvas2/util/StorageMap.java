package canvas2.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * キーの種類に制限がかけられ、変更時にリスナーを実行できるマップ。
 * {@link Collection}を継承していない。
 */
public class StorageMap<K> {

	private Map<K, Entry> data = this.createMap();
	private boolean canThrow;

	public StorageMap(boolean canThrow)
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


		this.fireChange(key, prev, value, true);
		e.setValue(value);
		this.data.put(key, e);
		this.fireChange(key, prev, value, false);

		return true;
	}





	/**
	 * 指定したキーの値を取得する。<br>
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
		return this.get(key, Object.class, def);
	}

	public <V> V get(K key, Class<V> c)
	{
		return CastUtil.cast(this.get(key));
	}

	public <V> V get(K key, Class<V> c, Supplier<V> def)
	{
		V output = this.get(key, c);
		if(output == null)
		{
			return def.get();
		}

		return output;
	}




	public void remove(K key)
	{
		Object prev = this.get(key);

		this.fireChange(key, prev, null, true);
		this.data.remove(key);
		this.fireChange(key, prev, null, false);
	}

	public void setNull(K key)
	{
		this.set(key, null);
	}

	protected void fireChange(K key, Object prev, Object next, boolean isBefore)
	{
		Entry e = this.data.get(key);
		if(e == null || e.getListener() == null)
		{
			return;
		}

		e.getListener().onChange(this, key, prev, next, isBefore);
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

	/**
	 * 値変更時に呼び出されるリスナー<br>
	 * {@link ChangeListener#onChange}
	 */
	public interface ChangeListener<K> {

		public void onChange(StorageMap<K> src, K key, Object prev, Object next, boolean isBefore);
	}







}
