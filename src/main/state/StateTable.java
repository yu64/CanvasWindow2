package main.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BooleanSupplier;

import main.debug.TextTree;
import main.logic.Updatable;

public class StateTable<S extends State> implements Updatable, TextTree{

	private S now;
	private Map<S, Map<S, Set<BooleanSupplier>>> table = new HashMap<>();
	private Map<S, Runnable> action = new HashMap<>();


	public StateTable(S initState)
	{
		this.now = initState;
	}

	public void register(S now, S next, BooleanSupplier condition)
	{
		Map<S, Set<BooleanSupplier>> map = this.table.get(now);
		if(map == null)
		{
			map = new HashMap<>();
		}

		Set<BooleanSupplier> set = map.get(next);
		if(set == null)
		{
			set = new HashSet<>();
		}

		set.add(condition);

		map.put(next, set);
		this.table.put(now, map);
	}

	public void register(S now, Runnable action)
	{
		this.action.put(now, action);
	}

	public S getState()
	{
		return this.now;
	}

	public void nextState(S next)
	{
		this.now = next;
	}

	@Override
	public void update(float tpf)
	{

		Runnable action = this.action.get(this.now);
		if(action != null)
		{
			action.run();
		}

		Map<S, Set<BooleanSupplier>> map = this.table.get(this.now);
		if(map == null)
		{
			return;
		}

		S next = null;
		for(Entry<S, Set<BooleanSupplier>> e : map.entrySet())
		{
			next = this.getNextState(e.getKey(), e.getValue());
			if(next != null)
			{
				this.nextState(next);
				return;
			}
		}

	}


	private S getNextState(S next, Set<BooleanSupplier> v)
	{
		for(BooleanSupplier b : v)
		{
			if(b.getAsBoolean())
			{
				return next;
			}
		}

		return null;
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);
		String title = this.getClass().getSimpleName();

		sb.append(tab1).append(title).append(enter);

		String tab2 = tab1 + "\t";
		String tab3 = tab1 + "\t\t";
		String tab4 = tab1 + "\t\t\t";
		String tab5 = tab1 + "\t\t\t";

		sb.append(tab2).append("[Transition]").append(enter);

		for(Entry<S, Map<S, Set<BooleanSupplier>>> e1 : this.table.entrySet())
		{
			sb.append(tab3).append(e1.getKey()).append(enter);

			for(Entry<S, Set<BooleanSupplier>> e2 : e1.getValue().entrySet())
			{
				sb.append(tab4).append(e2.getKey()).append(enter);

				for(BooleanSupplier s : e2.getValue())
				{
					sb.append(tab5).append(s).append(enter);
				}
			}
		}

		sb.append(tab2).append("[Action]").append(enter);

		for(Entry<S, Runnable> e : this.action.entrySet())
		{
			sb.append(tab3).append(e.getKey()).append(enter);
			sb.append(tab4).append(e.getValue()).append(enter);
		}

		sb.append(enter);

		return sb;
	}


}
