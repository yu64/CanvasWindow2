package canvas2.state;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import canvas2.core.Updatable;
import canvas2.debug.TextTree;
import canvas2.state.obj.State;

/**
 * 状態遷移表を示す。更新可能。
 */
public class StateTable<S extends State> implements Updatable, TextTree{

	private S now;
	private Map<S, Map<S, BooleanSupplier>> table = new HashMap<>();
	private Map<S, Map<S, Updatable>> change = new HashMap<>();
	private Map<S, Updatable> action = new HashMap<>();
	private Deque<S> transferDeque = new ArrayDeque<>();


	public StateTable(S initState)
	{
		Objects.requireNonNull(initState);

		this.now = initState;
		this.table = this.createMap();
		this.change = this.createMap();
		this.action = this.createMap();
		this.transferDeque = this.createDeque();
	}

	protected <A, B> Map<A, B> createMap()
	{
		return new HashMap<>();
	}

	protected Deque<S> createDeque()
	{
		return new ArrayDeque<>();
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
		Objects.requireNonNull(next);

		this.transferDeque.addFirst(this.now);
		this.transferDeque.addFirst(next);

		this.now = next;
	}

	@Override
	public void update(float tpf)
	{

		//状態に応じて常に更新
		Updatable action = this.action.get(this.now);
		if(action != null)
		{
			action.updateAndThrow(tpf);
		}

		//状態遷移
		Map<S, BooleanSupplier> map = this.table.get(this.now);
		if(map == null)
		{
			return;
		}

		for(Entry<S, BooleanSupplier> e : map.entrySet())
		{
			if(!e.getValue().getAsBoolean())
			{
				continue;
			}

			//遷移
			this.nextState(e.getKey());
			break;
		}


		//遷移時の処理を実行。
		while(!this.transferDeque.isEmpty())
		{
			S ori = this.transferDeque.pollLast();
			S dest = this.transferDeque.pollLast();

			this.doTransferAction(tpf, ori, dest);
		}

	}

	protected void doTransferAction(float tpf, S ori, S dest)
	{

		Map<S, Updatable> change = this.change.get(ori);
		if(change == null)
		{
			return;
		}

		Updatable u = change.get(dest);
		if(u == null)
		{
			return;
		}

		u.updateAndThrow(tpf);
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
		String tab5 = tab1 + "\t\t\t\t";

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
