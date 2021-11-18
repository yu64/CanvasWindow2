package canvas2.event.awt;

import java.awt.AWTEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import canvas2.core.event.Listener;
import canvas2.event.basic.BasicDispatcher;

/**
 * {@link AwtListener}を管理するディスパッチャー<br>
 * 識別子にAWTイベントIDを指定できる。
 */
public class AwtDispatcher extends BasicDispatcher<AWTEvent>{


	private Map<Integer, Set<Listener<AWTEvent>>> local;

	public AwtDispatcher()
	{
		super(AWTEvent.class);
		this.local = this.createMap();
	}

	protected <K, V> Map<K, V> createMap()
	{
		return new HashMap<>();
	}


	@Override
	public void addListener(Object exId, Listener<AWTEvent> listener)
	{
		if(exId == null)
		{
			super.addListener(exId, listener);
			return;
		}


		if( !(exId instanceof Integer))
		{
			return;
		}

		Set<Listener<AWTEvent>> set = this.local.get(exId);
		if(set == null)
		{
			set = new HashSet<>();
		}

		set.add(listener);

		this.local.put((Integer)exId, set);
	}

	@Override
	public void removeListener(Object exId, Listener<AWTEvent> listener)
	{
		if(exId == null)
		{
			super.removeListener(exId, listener);
			return;
		}

		if( !(exId instanceof Integer))
		{
			return;
		}

		Set<Listener<AWTEvent>> set = this.local.get(exId);
		if(set == null)
		{
			return;
		}

		set.remove(listener);

		this.local.put((Integer)exId, set);
	}

	@Override
	public void clearListener()
	{
		super.clearListener();
		this.local.clear();
	}

	@Override
	public void dispatch(float tpf, AWTEvent event)
	{
		super.dispatch(tpf, event);

		Set<Listener<AWTEvent>> set = this.local.get(event.getID());
		if(set == null)
		{
			return;
		}

		for(Listener<AWTEvent> h : set)
		{
			h.actAndThrow(tpf, event);
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{


		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String tab2 = tab1 + "\t";
		String tab3 = tab1 + "\t\t";
		String tab4 = tab1 + "\t\t\t";

		String title = this.getClass().getSimpleName();
		sb.append(tab1).append(title).append(enter);


		sb.append(tab2).append("[Global]").append(enter);
		for(Listener<AWTEvent> h : this.getListeners())
		{
			sb.append(tab3).append(h).append(enter);
		}

		sb.append(tab2).append("[Local]").append(enter);
		for(Entry<Integer, Set<Listener<AWTEvent>>> e : this.local.entrySet())
		{
			sb.append(tab3).append(e.getKey()).append(enter);

			for(Listener<AWTEvent> h : e.getValue())
			{
				sb.append(tab4).append(h).append(enter);
			}
		}

		return sb;
	}

}
