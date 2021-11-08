package canvas2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import canvas2.debug.TextTree;

public class MultiKeyMap<K, V> implements TextTree{
	
	
	private int dimensionSize;
	private int maxIndex;
	
	private Map<K, ?> root;

	private IntFunction<K[]> arrayFactory;
	private Supplier<Map<K, ?>> mapFactory;
	
	public MultiKeyMap(int dimension)
	{
		this(dimension, HashMap::new);
	}

	
	public MultiKeyMap(int dimension, Supplier<Map<K, ?>> mapFactory)
	{
		if(dimension <= 0)
		{
			throw new RuntimeException("Dimensions must be positive");
		}
		
		this.dimensionSize = dimension;
		this.maxIndex = dimension - 1;
		
		this.arrayFactory = (CastUtil::createArray);
		this.mapFactory = mapFactory;

		this.root = this.mapFactory.get();
	}
	
	protected List<K> createKeyList(int size, K[] keys)
	{
		List<K> list = new ArrayList<>(size);
		
		if(keys == null)
		{
			return list;
		}
		
		for(K key : keys)
		{
			list.add(key);
		}
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public void put(V value, K... keys)
	{
		this.put(keys, value);
	}
	
	public void put(K key, V value)
	{
		K[] keys = this.arrayFactory.apply(1);
		keys[0] = key;
		
		this.put(keys, value);
	}
	
	public void put(K[] keys, V value)
	{
		this.checkFullKeys(keys);
		
		this.updateElement(keys, (o, i, t) -> {
			
			
			if(o == null && t.isMap())
			{
				return this.mapFactory.get();
			}
			
			if(t == Type.VALUE)
			{
				return value;
			}
			
			return o;
		});
		
	}
	
	
	
	public void remove(K key)
	{
		K[] keys = this.arrayFactory.apply(1);
		keys[0] = key;
		
		this.remove(keys);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(K... keys)
	{
		this.checkFullKeys(keys);
		
		this.updateElement(keys, (o, i, t) -> {
			
			if(t == Type.VALUE)
			{
				return null;
			}
			
			return o;
		});
	}
	
	
	public void get(K key)
	{
		K[] keys = this.arrayFactory.apply(1);
		keys[0] = key;
		
		this.get(keys);
	}
	
	@SuppressWarnings("unchecked")
	public V get(K... keys)
	{
		this.checkFullKeys(keys);
		
		//修正したほうがよい。
		Object[] output = {null};
		
		this.updateElement(keys, (o, i, t) -> {
			
			if(t == Type.VALUE)
			{
				output[0] = o;
			}
			
			return o;
		});
		
		return CastUtil.cast(output[0]);
	}
	
	
	
	
	
	public void forEach(ValueAction<K, V> action)
	{
		List<K> list = this.createKeyList(this.dimensionSize, null);
		
		//ルートのインデックスは、0
		this.forEach(list, this.root, 0, action);
	}
	
	@SuppressWarnings("unchecked")
	public void forEach(ValueAction<K, V> action, K... keys)
	{
		this.forEach(keys, action);
	}

	public void forEach(K key, ValueAction<K, V> action)
	{
		K[] keys = this.arrayFactory.apply(1);
		keys[0] = key;
		
		this.forEach(action, keys);
	}
	
	public void forEach(K[] keys, ValueAction<K, V> action)
	{
		int keySize = keys.length;
		
		//キーがないときは、最初からループ。
		if(keys == null || keySize == 0)
		{
			this.forEach(action);
			return;
		}
		
		//キーが次元数より大きい場合、
		if(keySize > this.dimensionSize)
		{
			throw new RuntimeException("Incorrect key size");
		}
		
		//キーが次元数と等しいとき。
		//つまり、完全に一つの値を指しているとき。
		if(keySize == this.dimensionSize)
		{
			K lastKey = keys[keySize - 1];
			Objects.requireNonNull(lastKey);
			
			V value = this.get(keys);
			
			List<K> keyList = this.createKeyList(keys.length, keys);
			action.action(keyList, value);
			
			return;
		}
		
		
		Map<K, ?> start = this.getMap(keys);
		if(start == null)
		{
			//キーの場所が存在しないとき、終了
			return;
		}
		

		
		List<K> keyList = this.createKeyList(this.dimensionSize, keys);

		//N個のキーを適用した場合のインデックスは、N
		this.forEach(keyList, start, keySize, action);
	}
	
	
	private void forEach(List<K> log, Map<K, ?> map, int index, ValueAction<K, V> c1)
	{
		this.forEach(log, map, index, c1, (k, m, i) -> {});
	}
	
	private void forEach(List<K> log, Map<K, ?> map, int index, ValueAction<K, V> c1, MapAction<K> c2)
	{
		Type type = Type.getType(index, this.maxIndex);
		
		//Mapではないとき。
		if(!type.isMap())
		{
			return;
		}
		
		//最後のMapであるとき。
		if(type == Type.LAST_MAP)
		{
			Map<K, V> now = CastUtil.cast(map);
			for(Entry<K, V> e : now.entrySet())
			{
				log.add(e.getKey());
				c1.action(log, e.getValue());
				log.remove(log.size() - 1);
			}
			
			return;
		}
		
		//Mapであるとき
		
		Map<K, ?> now = map;
		for(Entry<K, ?> e : now.entrySet())
		{
			Map<K, ?> next = CastUtil.cast(e.getValue());
			log.add(e.getKey());
			
			c2.action(log, next, index + 1);
			this.forEach(log, next, index + 1, c1, c2);
			
			log.remove(log.size() - 1);
		}
	}
	
	
	
	
	
	
	
	private void checkFullKeys(K[] keys)
	{
		Objects.requireNonNull(keys);
		
		if(keys.length != this.dimensionSize)
		{
			throw new RuntimeException("Incorrect key size");
		}
	
		K last = keys[keys.length - 1];
		Objects.requireNonNull(last);
		
	}

	private Map<K, ?> getMap(K[] keys)
	{
		//修正したほうがよい。
		List<Object> output = new ArrayList<>(1);
		output.add(null);
		
		this.updateElement(keys, (o, i, t) -> {
			
			if(t.isMap())
			{
				output.set(0, o);
			}
			return o;
		});
		
		return CastUtil.cast(output.get(0));
	}
	
	
	
	public void updateElement(K[] keys, Action a)
	{
		this.root = this.updateElement(this.root, 0, keys, a);
	}
	
	private <T, O> T updateElement(T now, int index, K[] keys, Action a)
	{
		//戻り値は、対象が置き換わる値。
		
		//対象のタイプを求める。
		Type type = Type.getType(index, this.maxIndex);
		
		//対象を更新する。戻り値は、置き換えたい対象の値。
		now = CastUtil.cast(a.change(now, index, type));
		
		
		
		//対象がnullに置き換わるようにする。
		//nullに置き換わるので、先の探索は不要。
		if(now == null)
		{
			return null;
		}
		
		//キーが不足しているとき、これ以上、先に探索できない。
		if(index > keys.length - 1)
		{
			return now;
		}
		
		//Mapではないならば、これ以上、先に探索できない。
		if(!type.isMap())
		{
			return now;
		}
		
		
		//Mapならば、配下も更新する。
		
		//次の要素を取得するためのキー
		K key = keys[index];
		
		Map<K, O> tempNowMap = CastUtil.cast(now);
		O next = tempNowMap.get(key);
		O tempNext = next;
		
		//配下を更新する。置き換えたい、配下の値を取得。
		next = this.updateElement(next, index + 1, keys, a);
		
		//値を置き換える必要がないとき、
		if(next == tempNext)
		{
			return now;
		}
		
		//置き換えたい配下の値がnullではないとき、
		if(next != null)
		{
			tempNowMap.put(key, next);
			return now;
		}
		
		//置き換えたい配下の値がnullであるとき、削除。
		tempNowMap.remove(key);
		
		//対象の中味が削除によって空になったとき、
		//対象がnullに置き換わるようにする。
		if(tempNowMap.isEmpty())
		{
			return null;
		}
		
		return now;
	}
	
	
	
	@Override
	public String toString()
	{
		String name = this.getClass().getSimpleName();
		int d = this.dimensionSize;
		String map = this.root.toString();
		
		return name + "[" + d + "_D]{" + map + "}";
	}
	
	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String title = this.getClass().getSimpleName();
		String mapText = this.root.getClass().getSimpleName();
		
		sb.append(tab1).append(title);
		sb.append("(").append(mapText).append(")").append(enter);
		
		
		List<K> list = this.createKeyList(this.dimensionSize, null);
		this.forEach(list, this.root, 0, (k, v) -> {
			
			K lastKey = k.get(k.size() - 1);
			String tab2 = tab1 + "\t".repeat(this.dimensionSize);
			sb.append(tab2);
			sb.append(lastKey).append(" = ").append(v).append(enter);
			
		}, (k, m, i) -> {
			
			K lastKey = k.get(k.size() - 1);
			String tab2 = tab1 + "\t".repeat(i);
			String text = m.getClass().getSimpleName();
			
			sb.append(tab2);
			sb.append(lastKey).append(" = ").append(text).append(enter);
			
		});
		
		
		return sb;
	}
	
	
	protected static enum Type {
		
		ROOT,
		INNER_MAP,
		LAST_MAP,
		VALUE,
		
		
		;
		
		public static Type getType(int index, int maxIndex)
		{
			if(index == 0)
			{
				return Type.ROOT;
			}
			else if(index == maxIndex)
			{
				return Type.LAST_MAP;
			}
			else if(index < maxIndex)
			{
				return Type.INNER_MAP;
			}
			
			return Type.VALUE;
		}
		
		public boolean isMap()
		{
			return (this == Type.ROOT || this == Type.INNER_MAP || this == Type.LAST_MAP);
		}
		
	}
	
	/** Object, int, Type **/
	@FunctionalInterface
	protected static interface Action {
		
		public Object change(Object o, int index, Type type);
	}
	
	
	/** K, Map<K, ?>. int **/
	@FunctionalInterface
	protected static interface MapAction<K>{
		
		public void action(List<K> keys, Map<K, ?> m, int d);
		
	}
	
	/** K key, V value, int index **/
	@FunctionalInterface
	public static interface ValueAction<K, V>{
		
		public void action(List<K> keys, V value);
	}
	
	
	
}
