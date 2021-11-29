package canvas2.state;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;

import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.core.event.Listener;
import canvas2.state.obj.State;
import canvas2.util.MultiKeyMap;

/**
 * 状態遷移表を示す。更新可能。
 */
public class StateTable<S extends State>
	implements Updatable, Listener<EventObject>, TextTree{

	private S now;

	private ChangeListener<S> global;

	private Set<S> nextKind;
	private MultiKeyMap<S, PairEntry<S>> table;
	private Map<S, StateEntry<S>> info;



	public StateTable(S initState)
	{
		this.nextKind = this.createSet();
		this.table = this.createMultiMap();
		this.info = this.createMap();

		this.reset(initState);
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

	protected StateEntry<S> createEntry(S state)
	{
		return new StateEntry<>(state);
	}

	protected PairEntry<S> createEntry(S now, S next)
	{
		return new PairEntry<>(now, next);
	}


	/**
	 * すべての設定を初期化します。
	 */
	public void reset(S initState)
	{
		Objects.requireNonNull(initState);

		this.table.clear();
		this.nextKind.clear();
		this.info.clear();
		this.now = initState;
	}

	/**
	 * すべての状態の変更時に呼び出される処理を登録します。
	 */
	public void setGlobalChange(ChangeListener<S> change)
	{
		this.global = change;
	}





	protected StateEntry<S> getStateEntry(S state, boolean canCreate)
	{
		StateEntry<S> entry = this.info.get(state);
		if(entry == null)
		{
			if(!canCreate)
			{
				return null;
			}

			entry = this.createEntry(state);
			this.info.put(state, entry);
		}

		return entry;
	}

	/**
	 * 指定した状態である間、実行される処理を登録する。
	 */
	public void setUpdate(S now, Updatable action)
	{
		StateEntry<S> e = this.getStateEntry(now, true);
		e.setUpdate(action);
	}

	/**
	 * 指定した状態になった後の処理を登録する。
	 */
	public void setEnterListener(S now, EnterListener<S> enter)
	{
		StateEntry<S> e = this.getStateEntry(now, true);
		e.setEnter(enter);
	}

	/**
	 * 指定した状態ではなくなる前の処理を登録する。
	 */
	public void setLeaveListener(S now, LeaveListener<S> leave)
	{
		StateEntry<S> e = this.getStateEntry(now, true);
		e.setLeave(leave);
	}

	/**
	 * 指定した状態で、イベントが発火したとき、処理することを登録する。
	 */
	public void setEventListener(S now, Listener<EventObject> event)
	{
		StateEntry<S> e = this.getStateEntry(now, true);
		e.setEventListener(event);
	}


	/**
	 * 指定した状態で、イベントが発火したとき、遷移を試みるように設定する。<br>
	 * {@link StateTable#setEventListener}を用いている。
	 */
	public void setMoveByEvent(S now)
	{
		this.setEventListener(now, (tpf, obj) -> this.tryMoveState());
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

			this.getStateEntry(now, true);
			this.getStateEntry(next, true);
			this.nextKind.add(next);
		}
	}

	/**
	 * 指定した状態かつ、指定した条件が真であるとき、<br>
	 * 次の指定した状態に遷移することを登録する。<br>
	 * {@link StateTable#allow}が実行される。<br>
	 */
	public void set(S now, S next, BooleanSupplier condition)
	{
		this.allow(now, next);

		PairEntry<S> e = this.table.get(now, next);
		e.setCondition(condition);
	}

	/**
	 * 指定した状態から、次の指定した状態に遷移したとき、
	 * 処理することを登録する。<br>
	 * {@link StateTable#allow}が実行される。<br>
	 */
	public void setChange(S now, S next, ChangeListener<S> change)
	{
		this.allow(now, next);
		PairEntry<S> e = this.table.get(now, next);

		e.setChangeListener(change);
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
	 * 現在の状態から遷移を試みる。
	 * 遷移条件を評価し、<u>遷移可能であるとき遷移する。</u>
	 */
	public boolean tryMoveState()
	{
		//遷移条件に従い、遷移を行う。
		for(S next : this.nextKind)
		{
			if(this.tryMoveState(next))
			{
				return true;
			}
		}

		return false;
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
	 * 遷移条件に関係なく強制的に状態を変更する。<br>
	 * リスナーは、実行される。<br>
	 * {@link ChangeListener}<br>
	 * {@link LeaveListener}<br>
	 * {@link EnterListener}<br>
	 */
	public void setState(S next)
	{
		this.setState(next, true);
	}

	/**
	 * 遷移条件に関係なく強制的に状態を変更する。<br>
	 * リスナーの実行が可能。<br>
	 * {@link ChangeListener}<br>
	 * {@link LeaveListener}<br>
	 * {@link EnterListener}<br>
	 */
	protected void setState(S next, boolean canInvokeListener)
	{
		Objects.requireNonNull(next);

		if(canInvokeListener)
		{
			this.fireBeforeChangeListner(this.now, next);
		}

		this.now = next;

		if(canInvokeListener)
		{
			this.fireAfterChangeListner(this.now, next);
		}
	}

	/**
	 * 状態変更前にリスナーを呼び出す。
	 */
	protected void fireBeforeChangeListner(S now, S next)
	{
		StateEntry<S> nowEntry = this.info.get(now);
		PairEntry<S> e = this.table.get(now, next);

		this.onChange(now, next, true);

		if(this.global != null)
		{
			this.global.onChange(this, now, next, true);
		}

		if(e != null && e.getChangeListener() != null)
		{
			e.getChangeListener().onChange(this, now, next, true);
		}

		if(nowEntry != null && nowEntry.getLeave() != null)
		{
			nowEntry.getLeave().onLeave(this, now, next);
		}
	}

	/**
	 * 状態変更後にリスナーを呼び出す。
	 */
	protected void fireAfterChangeListner(S prev, S now)
	{
		StateEntry<S> nowEntry = this.info.get(now);
		PairEntry<S> e = this.table.get(prev, now);

		if(nowEntry != null && nowEntry.getEnter() != null)
		{
			nowEntry.getEnter().onEnter(this, prev, now);
		}

		if(e != null && e.getChangeListener() != null)
		{
			e.getChangeListener().onChange(this, prev, now, false);
		}

		if(this.global != null)
		{
			this.global.onChange(this, prev, now, false);
		}

		this.onChange(prev, now, false);
	}

	/**
	 * 状態更新時に呼び出されるメソッド
	 */
	protected void onChange(S prev, S now, boolean isBefore)
	{

	}





	/**
	 * 現在の状態に対応した更新処理を行う。
	 */
	public void updateState(float tpf)
	{
		StateEntry<S> e = this.getStateEntry(this.getState(), false);
		if(e == null)
		{
			return;
		}

		Updatable action = e.getUpdate();
		if(action == null)
		{
			return;
		}

		action.updateOrThrow(tpf);
	}

	@Override
	public void update(float tpf)
	{
		this.updateState(tpf);
		this.tryMoveState();
	}




	@Override
	public void act(float tpf, EventObject e) throws Exception
	{
		StateEntry<S> entry = this.getStateEntry(this.getState(), false);
		if(entry == null)
		{
			return;
		}


		Listener<EventObject> h = entry.getEventListener();
		if(h == null)
		{
			return;
		}

		h.act(tpf, e);
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
			sb.append(tab6).append(e.getChangeListener()).append(enter);
			sb.append(enter);

		}

		sb.append(tab2).append("[State Info]").append(enter);

		for(StateEntry<S> e : this.info.values())
		{

			sb.append(tab3).append(e.getState()).append(enter);
			sb.append(tab4).append("[Enter]").append(enter);
			sb.append(tab5).append(e.getEnter()).append(enter);
			sb.append(tab4).append("[Leave]").append(enter);
			sb.append(tab5).append(e.getLeave()).append(enter);
			sb.append(tab4).append("[Update]").append(enter);
			sb.append(tab5).append(e.getUpdate()).append(enter);
			sb.append(tab4).append("[EventListener]").append(enter);
			sb.append(tab5).append(e.getEventListener()).append(enter);
			sb.append(enter);
		}


		return sb;
	}

	protected static class StateEntry<S extends State> {

		private S state;

		private Updatable update;
		private EnterListener<S> enter;
		private LeaveListener<S> leave;
		private Listener<EventObject> event;

		protected StateEntry(S state)
		{
			this.state = state;
		}

		public S getState()
		{
			return this.state;
		}

		public Updatable getUpdate()
		{
			return update;
		}

		public void setUpdate(Updatable update)
		{
			this.update = update;
		}

		public EnterListener<S> getEnter()
		{
			return enter;
		}

		public void setEnter(EnterListener<S> enter)
		{
			this.enter = enter;
		}

		public LeaveListener<S> getLeave()
		{
			return leave;
		}

		public void setLeave(LeaveListener<S> leave)
		{
			this.leave = leave;
		}

		public Listener<EventObject> getEventListener()
		{
			return event;
		}

		public void setEventListener(Listener<EventObject> event)
		{
			this.event = event;
		}


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

		public ChangeListener<S> getChangeListener()
		{
			return change;
		}

		public void setChangeListener(ChangeListener<S> change)
		{
			this.change = change;
		}

	}

	/**
	 * 状態変更時に呼び出される処理を示すクラス。<br>
	 * メソッド<br>
	 * {@link ChangeListener#onChange}
	 */
	@FunctionalInterface
	public static interface ChangeListener<S extends State> {

		public void onChange(StateTable<S> src, S prev, S next, boolean isBefore);

	}

	/**
	 * 特定の状態になった後に呼び出される処理を示すクラス。<br>
	 * メソッド<br>
	 * {@link EnterListener#onEnter}
	 */
	@FunctionalInterface
	public static interface EnterListener<S extends State> {

		public void onEnter(StateTable<S> src, S prev, S now);
	}

	/**
	 * 特定の状態にではなくなる前に呼び出される処理を示すクラス。<br>
	 * メソッド<br>
	 * {@link LeaveListener#onLeave}
	 */
	@FunctionalInterface
	public static interface LeaveListener<S extends State> {

		public void onLeave(StateTable<S> src, S now, S next);
	}




}
