package main.value;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import main.event.EventManager;
import main.event.awt.AwtListener;

public class KeyFlags implements AwtListener{



	private Map<Integer, Boolean> flag = new HashMap<>();
	private int trueCount = 0;

	@SafeVarargs
	public KeyFlags(Integer... keys)
	{
		Objects.requireNonNull(keys);

		for(Integer key : keys)
		{
			Objects.requireNonNull(key);
			this.flag.put(key, false);
		}
	}

	public KeyFlags(Set<Integer> keys)
	{
		Objects.requireNonNull(keys);

		for(Integer key : keys)
		{
			Objects.requireNonNull(key);
			this.flag.put(key, false);
		}
	}

	public void set(Integer key, boolean flag, boolean canThrow)
	{
		Boolean b = this.flag.get(key);
		Boolean target = (flag ? Boolean.TRUE : Boolean.FALSE);

		//存在するキーで、目的の値と異なるとき、値を変更する。
		if(b != null && b != target)
		{
			this.flag.put(key, flag);
			this.trueCount += (flag ? 1 : -1);
			return;
		}

		if(b == null && canThrow)
		{
			this.throwNotFindKey(key);
		}

	}

	public void enable(Integer key, boolean canThrow)
	{
		this.set(key, true, canThrow);
	}

	public void disable(Integer key, boolean canThrow)
	{
		this.set(key, false, canThrow);
	}

	public boolean isTrue(Integer key)
	{
		Boolean b = this.flag.get(key);
		if(b == null)
		{
			this.throwNotFindKey(key);
		}

		return b;
	}

	private void throwNotFindKey(Integer key)
	{
		throw new RuntimeException("not find : " + key);
	}



	public int getAllCount()
	{
		return this.flag.size();
	}

	public int getTrueCount()
	{
		return this.trueCount;
	}

	public int getFalseCount()
	{
		return this.flag.size() - this.trueCount;
	}


	public boolean isFullTrue()
	{
		return this.getTrueCount() == this.getAllCount();
	}

	public boolean isFullFalse()
	{
		return this.getFalseCount() == this.getAllCount();
	}



	public void registerTo(EventManager event)
	{
		event.add(AwtListener.class, KeyEvent.KEY_PRESSED, this);
		event.add(AwtListener.class, KeyEvent.KEY_RELEASED, this);
	}

	public void unregisterTo(EventManager event)
	{
		event.remove(AwtListener.class, KeyEvent.KEY_PRESSED, this);
		event.remove(AwtListener.class, KeyEvent.KEY_RELEASED, this);
	}

	@Override
	public void action(float tpf, AWTEvent v) throws Exception
	{
		if( !(v instanceof KeyEvent))
		{
			return;
		}

		KeyEvent e = (KeyEvent) v;

		boolean isPressed = (e.getID() == KeyEvent.KEY_PRESSED);
		this.set(e.getKeyCode(), isPressed, false);

	}

}
