package canvas2.state;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.state.obj.State;

/**
 * 状態遷移表を示す。更新可能。
 */
public class StateTable<S extends State> implements Updatable, TextTree{

	private S now;

	private Map<S, Entry> table = new HashMap<>();
	private Map<S, Updatable> action = new HashMap<>();


	public StateTable(S initState)
	{
		Objects.requireNonNull(initState);

		this.now = initState;
		this.table = this.createMap();
		this.action = this.createMap();
	}

	protected <A, B> Map<A, B> createMap()
	{
		return new HashMap<>();
	}

	protected Deque<S> createDeque()
	{
		return new ArrayDeque<>();
	}

	protected Entry createEntry(S now, S next)
	{
		return new Entry(now, next);
	}

	/**
	 * 指定した状態かつ、指定した条件が真であるとき、<br>
	 * 次の指定した状態に遷移することを登録する。
	 */
	public void register(S now, S next, BooleanSupplier condition)
	{
		this.allow(now, next);

		Entry e = this.table.get(now);
		e.setCondition(condition);
	}

	/**
	 * 指定した状態から、次の指定した状態に遷移することを登録する。<br>
	 * この時、遷移条件は、存在しない。
	 */
	public void allow(S now, S next)
	{
		Entry e = this.table.get(now);
		if(e != null)
		{
			e = this.createEntry(now, next);
			this.table.put(now, e);
		}
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
	public void register(S now, S next, ChangeListener<S> change)
	{
		this.allow(now, next);
		Entry e = this.table.get(now);

		e.setChange(change);
	}


	/**
	 * 現在の状態を取得する。
	 */
	public S getState()
	{
		return this.now;
	}

	/**
	 * 指定の状態へ遷移を試みる。<br>
	 * 遷移条件を評価し、遷移可能であるとき遷移する。
	 */
	public boolean tryMoveState(S next)
	{
		Map<S, BooleanSupplier> map = this.table.get(this.now);
		if(map == null)
		{
			return false;
		}

		BooleanSupplier c = map.get(next);
		if(c != null && c.getAsBoolean())
		{
			this.setState(next);
			return true;
		}

		return false;
	}

	/**
	 * 遷移条件に関係なく遷移する。<br>
	 * ただし、遷移が登録されていないければならない。
	 */
	public void moveState(S next, boolean canThrow)
	{
		Entry e = this.table.get(this.now);
		if(e == null)
		{
			if(canThrow)
			{
				this.throwNotAllowed(next);
			}

			return;
		}

		this.setState(next);
	}

	protected void throwNotAllowed(S next)
	{
		throw new RuntimeException("Not allowed. " + next);
	}


	/**
	 * 遷移条件に関係なく強制的に状態を変更する。
	 */
	public void setState(S next)
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
			this.setState(e.getKey());
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

	public class Entry {

		private S prev;
		private S next;

		private BooleanSupplier condition;
		private ChangeListener<S> change;

		protected Entry(S prev, S next)
		{
			this.prev = prev;
			this.next = next;
		}

		public S getPrev()
		{
			return prev;
		}

		public S getNext()
		{
			return next;
		}

		public BooleanSupplier getCondition()
		{
			return condition;
		}

		public void setCondition(BooleanSupplier condition)
		{
			this.condition = condition;
		}

		public ChangeListener<S> getChange()
		{
			return change;
		}

		public void setChange(ChangeListener<S> change)
		{
			this.change = change;
		}
	}

	public static interface ChangeListener<S extends State> {

		public void onChange(StateTable<S> src, S next, S prev);

	}


}
