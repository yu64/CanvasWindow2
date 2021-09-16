package main.event.awt;

import java.awt.AWTEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import main.event.Dispatcher;

public class AwtDispatcher implements Dispatcher<AWTEvent, AwtListener>{

	private Map<Integer, Set<AwtListener>> action = new ConcurrentHashMap<>();

	@Override
	public Class<AWTEvent> getEventClass()
	{
		return AWTEvent.class;
	}

	@Override
	public Class<AwtListener> getListenerClass()
	{
		return AwtListener.class;
	}

	@Override
	public void addListener(Object exId, AwtListener listener)
	{
		if( !(exId instanceof Integer))
		{
			return;
		}
		
		Set<AwtListener> set = this.action.get(exId);
		if(set == null)
		{
			set = new HashSet<>();
		}
		
		set.add(listener);
		
		this.action.put((Integer)exId, set);
	}
	
	@Override
	public void removeListener(Object exId, AwtListener listener)
	{
		Set<AwtListener> set = this.action.get(exId);
		if(set == null)
		{
			return;
		}
		
		set.remove(listener);
		
		if(set.isEmpty())
		{
			return;
		}
		
		this.action.put((Integer)exId, set);
	}

	@Override
	public void clearListener()
	{
		this.action.clear();
	}
	

	@Override
	public void dispatch(float tpf, AWTEvent event)
	{
		
		Set<AwtListener> set =  this.action.get(event.getID());
		if(set == null)
		{
			return;
		}
		
		try
		{
			for(AwtListener listener : set)
			{
				listener.action(tpf, event);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String tab2 = tab1 + "\t";
		String title = this.getClass().getSimpleName();
		
		sb.append(tab1).append(title).append(enter);
		
		for(Entry<Integer, Set<AwtListener>> e : this.action.entrySet())
		{
			sb.append(tab2).append(e.getKey()).append(enter);
			
			String tab3 = tab2 + "\t";
			for(AwtListener listener : e.getValue())
			{
				sb.append(tab3).append(listener).append(enter);
			}
			
		}
		
		return sb;
	}


}
