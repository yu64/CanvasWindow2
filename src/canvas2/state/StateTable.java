package canvas2.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;

import canvas2.core.Updatable;
import canvas2.debug.TextTree;

/**
 * 状態遷移表を示す。更新可能。
 */
public class StateTable<S extends State> implements Updatable, TextTree{

	private S now;
	private Map<S, Map<S, BooleanSupplier>> table = new HashMap<>();
	private Map<S, Map<S, Updatable>> change = new HashMap<>();
	private Map<S, Updatable> action = new HashMap<>();


	public StateTable(S initState)
	{
		this.now = initState;
	}

	protected <A, B> Map<A, B> createMap()
	{
		return new HashMap<>();
	}


	/**
	 * 指定した状態かつ、指定した条件が真であるとき、<br>
	 * 次の指定した状態に遷移することを登録する。
	 */
	public void register(S now, S next, BooleanSupplier condition)
	{
		Map<S, BooleanSupplier> map = this.table.get(now);
		if(map == null)
		{
			map = this.createMap();
		}

		map.put(next, condition);
		this.table.put(now, map);
	}

	/**
	 * 指定した状態である間、実行される処理を登録する。
	 */
	public void register(S now, Updatable action)
	{
		this.action.put(now, action);
	}

	/**
	 * 指定した状態から、次の指定した状態に遷移したとき、
	 * 処理することを登録する。
	 */
	public void register(S now, S next, Updatable condition)
	{
		Map<S, Updatable> map = this.change.get(now);
		if(map == null)
		{
			map = this.createMap();
		}

		map.put(next, condition);
		this.change.put(now, map);
	}


	/**
	 * 現在の状態を取得する。
	 */
	public S getState()
	{
		return this.now;
	}

	/**
	 * 強制的に状態を変更する。
	 */
	public void nextState(S next)
	{
		this.now = next;
	}

	@Override
	public void update(float tpf)
	{

		//状態に応じて常に更新
		Updatable action = this.action.get(this.now);
		if(action != null)
		{
			try
			{
				action.update(tpf);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}

		Map<S, BooleanSupplier> map = this.table.get(this.now);
		if(map == null)
		{
			return;
		}

		Map<S, Updatable> change = this.change.get(this.now);


		//状態遷移
		for(Entry<S, BooleanSupplier> e : map.entrySet())
		{
			if(!e.getValue().getAsBoolean())
			{
				continue;
			}

			//遷移時の処理
			if(change != null)
			{
				Updatable u = change.get(e.getKey());

				try
				{
					u.update(tpf);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}

			//遷移
			this.nextState(e.getKey());
			return;
		}

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

		for(Entry<S, Map<S, BooleanSupplier>> e1 : this.table.entrySet())
		{
			sb.append(tab3).append(e1.getKey()).append(enter);

			for(Entry<S, BooleanSupplier> e2 : e1.getValue().entrySet())
			{
				sb.append(tab4).append(e2.getKey()).append(enter);
				sb.append(tab5).append(e2.getValue()).append(enter);

			}
		}



		sb.append(tab2).append("[Action]").append(enter);

		for(Entry<S, Updatable> e : this.action.entrySet())
		{
			sb.append(tab3).append(e.getKey()).append(enter);
			sb.append(tab4).append(e.getValue()).append(enter);
		}



		sb.append(tab2).append("[Change]").append(enter);

		for(Entry<S, Map<S, Updatable>> e1 : this.change.entrySet())
		{
			sb.append(tab3).append(e1.getKey()).append(enter);

			for(Entry<S, Updatable> e2 : e1.getValue().entrySet())
			{
				sb.append(tab4).append(e2.getKey()).append(enter);
				sb.append(tab5).append(e2.getValue()).append(enter);

			}
		}

		sb.append(enter);



		return sb;
	}


}
