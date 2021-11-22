package canvas2.state;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;

import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.state.obj.State;
import canvas2.util.MultiKeyMap;

/**
 * 状態遷移表を示す。更新可能。
 */
public class StateTable<S extends State> implements Updatable, TextTree{

	private S now;

	private Set<S> nextKind;
	private MultiKeyMap<S, PairEntry<S>> table;
	private Map<S, Updatable> action;


	public StateTable(S initState)
	{
		Objects.requireNonNull(initState);

		this.now = initState;
		this.nextKind = this.createSet();
		this.table = this.createMultiMap();
		this.action = this.createMap();
	}

	protected <A> Set<A> createSet()
	{
		return new HashSet<>();
	}

	protected <A, B> Map<A, B> createMap()
	{
		return new HashMap<>();
	}

	protected <A, B> MultiKeyMap<A, B> createMultiMap()
	{
		return new MultiKeyMap<>(2);
	}

	protected Deque<S> createDeque()
	{
		return new ArrayDeque<>();
	}

	protected PairEntry<S> createEntry(S now, S next)
	{
		return new PairEntry<>(now, next);
	}




	/**
	 * 指定した状態かつ、指定した条件が真であるとき、<br>
	 * 次の指定した状態に遷移することを登録する。
	 */
	public void set(S now, S next, BooleanSupplier condition)
	{
		this.allow(now, next);

		PairEntry<S> e = this.table.get(now, next);
		e.setCondition(condition);
	}

	/**
	 * 指定した状態から、次の指定した状態に遷移することを登録する。<br>
	 * この時、遷移条件は、存在しない。
	 */
	public void allow(S now, S next)
	{
		PairEntry<S> e = this.table.get(now, next);
		if(e == null)
		{
			e = this.createEntry(now, next);
			this.table.put(e, now, next);

			this.nextKind.add(next);
		}
	}

	/**
	 * 指定した状態である間、実行される処理を登録する。
	 */
	public void setUpdate(S now, Updatable action)
	{
		this.action.put(now, action);
	}

	/**
	 * 指定した状態から、次の指定した状態に遷移したとき、
	 * 処理することを登録する。
	 */
	public void setChange(S now, S next, ChangeListener<S> change)
	{
		this.allow(now, next);
		PairEntry<S> e = this.table.get(now, next);

		e.setChange(change);
	}

	/**
	 * 指定の遷移が許可されているか。
	 */
	public boolean isAllowed(S now, S next)
	{
		if(!this.nextKind.contains(next))
		{
			return false;
		}

		PairEntry<S> e = this.table.get(now, next);
		if(e == null)
		{
			return false;
		}

		return true;
	}




	/**
	 * 現在の状態を取得する。
	 */
	public S getState()
	{
		return this.now;
	}

	/**
	 * 現在の状態が、指定した状態であるか。
	 */
	public boolean equalsState(S s)
	{
		return Objects.equals(this.now, s);
	}

	/**
	 * 指定の状態へ遷移を試みる。<br>
	 * 遷移条件を評価し、<u>遷移可能であるとき遷移する。</u>
	 */
	public boolean tryMoveState(S next)
	{
		Objects.requireNonNull(next);

		if(!this.nextKind.contains(next))
		{
			return false;
		}

		PairEntry<S> e = this.table.get(this.now, next);
		if(e == null)
		{
			return false;
		}

		BooleanSupplier c = e.getCondition();
		if(c == null)
		{
			return false;
		}

		if(c.getAsBoolean())
		{
			this.setState(next);
			return true;
		}

		return false;
	}

	/**
	 * 遷移条件に関係なく遷移する。<br>
	 * ただし、遷移が登録されていないければならない。<br>
	 * 登録されていない場合、遷移しない。
	 */
	public void moveState(S next, boolean canThrow)
	{
		Objects.requireNonNull(next);

		if(!this.isAllowed(this.now, next))
		{
			if(canThrow)
			{
				this.throwNotAllowed(next);
				return;
			}
		}

		this.setState(next);
	}

	protected void throwNotAllowed(S next)
	{
		throw new RuntimeException("Not allowed. " + this.now + " ==> " + next);
	}


	/**
	 * 遷移条件に関係なく強制的に状態を変更する。
	 */
	public void setState(S next)
	{
		Objects.requireNonNull(next);

		PairEntry<S> e = this.table.get(this.now, next);
		this.now = next;

		if(e != null && e.getChange() != null)
		{
			e.getChange().onPostChange(this, this.now, next);
			this.onPostChange(this.now, next);
		}

	}




	/**
	 * 現在の状態に対応した更新処理を行う。
	 */
	public void updateState(float tpf)
	{
		Updatable action = this.action.get(this.now);
		if(action != null)
		{
			action.updateAndThrow(tpf);
		}
	}

	@Override
	public void update(float tpf)
	{
		this.updateState(tpf);

		//遷移条件に従い、遷移を行う。
		for(S next : this.nextKind)
		{
			if(this.tryMoveState(next))
			{
				break;
			}
		}

	}

	protected void onPostChange(S prev, S now)
	{

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
		String tab6 = tab1 + "\t\t\t\t\t";

		sb.append(tab2).append("[Allowed Transition]").append(enter);

		for(PairEntry<S> e : this.table.getValues())
		{

			sb.append(tab3).append(e.getNow()).append(enter);
			sb.append(tab4).append(e.getNext()).append(enter);
			sb.append(tab5).append("[Conditions]").append(enter);
			sb.append(tab6).append(e.getCondition()).append(enter);
			sb.append(tab5).append("[ChangeListener]").append(enter);
			sb.append(tab6).append(e.getChange()).append(enter);
			sb.append(enter);

		}

		sb.append(tab2).append("[Update Action]").append(enter);

		for(Entry<S, Updatable> e : this.action.entrySet())
		{
			sb.append(tab3).append(e.getKey()).append(enter);
			sb.append(tab4).append(e.getValue()).append(enter);
		}


		return sb;
	}

	protected static class PairEntry<S extends State> {

		private S now;
		private S next;

		private BooleanSupplier condition;
		private ChangeListener<S> change;

		protected PairEntry(S now, S next)
		{
			this.now = now;
			this.next = next;
		}

		public S getNow()
		{
			return now;
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

		public void onPostChange(StateTable<S> src, S prev, S now);

	}


}
