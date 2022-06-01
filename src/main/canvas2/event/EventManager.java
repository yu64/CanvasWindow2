package canvas2.event;

import java.util.ArrayDeque;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

import canvas2.core.Updatable;
import canvas2.core.debug.TextTree;
import canvas2.core.event.Listener;
import canvas2.core.event.Trigger;
import canvas2.event.basic.BasicDispatcher;
import canvas2.util.CastUtil;

/**
 * イベントを管理する。<br>
 * イベントの種類、イベントに対応するリスナーの登録、
 * リスナーを判別するディスパッチャーの登録など
 */
public class EventManager implements Updatable, TextTree{

	private Queue<EventObject> queue;
	private Set<Dispatcher<?>> dispatchers = new HashSet<>();


	public EventManager()
	{
		this.queue = this.createQueue();
	}


	protected Queue<EventObject> createQueue()
	{
		return new ArrayDeque<>();
	}

	protected <T> Set<T> createSet()
	{
		return new HashSet<>();
	}

	protected <T extends EventObject> Dispatcher<T> createDispatcher(Class<T> event)
	{
		return new BasicDispatcher<T>(event);
	}


	/**
	 * 指定したイベントを処理する、ディスパッチャーを設定する。<br>
	 * 設定されていない場合、標準のディスパッチャーが用いられる。<br>
	 */
	public <E extends EventObject> void setDispatcher(Class<E> e, Dispatcher<E> in)
	{
		Iterator<Dispatcher<?>> ite = this.dispatchers.iterator();
		while(ite.hasNext())
		{
			Dispatcher<?> d = ite.next();
			if(d.getEventClass() != e)
			{
				continue;
			}

			ite.remove();
		}

		this.dispatchers.add(in);
	}




	/**
	 * 指定のイベントのリスナーを追加する。
	 */
	public <E extends EventObject> void add(Class<E> e, Listener<? super E> listener)
	{
		this.add(e, null, listener);
	}

	/**
	 * 指定のイベントと識別子とリスナーを追加する。<br>
	 * 識別子の扱い方は、ディスパッチャーによって定義される。
	 */
	public <E extends EventObject> void add(Class<E> e, Object exId, Listener<? super E> listener)
	{
		boolean isAdded = false;
		for(Dispatcher<?> d : this.dispatchers)
		{
			if(!d.getEventClass().isAssignableFrom(e))
			{
				continue;
			}

			Dispatcher<E> d2 = CastUtil.cast(d);
			d2.addListener(exId, listener);
			isAdded = true;
		}

		if(isAdded)
		{
			return;
		}

		Dispatcher<E> d = this.createDispatcher(e);
		d.addListener(exId, listener);

		this.dispatchers.add(d);
	}


	/**
	 * 指定のイベントのリスナーを削除する。
	 */
	public <E extends EventObject> void remove(Class<E> e, Listener<? super E> listener)
	{
		this.add(e, null, listener);
	}

	/**
	 * 指定のイベントと識別子のリスナーを削除する。<br>
	 * 識別子の扱い方は、ディスパッチャーによって定義される。
	 */
	public <E extends EventObject> void remove(Class<E> e, Object exId, Listener<? super E> listener)
	{
		Iterator<Dispatcher<?>> ite = this.dispatchers.iterator();
		while(ite.hasNext())
		{
			Dispatcher<?> d = ite.next();
			if(!d.getEventClass().isAssignableFrom(e))
			{
				continue;
			}

			Dispatcher<E> d2 = CastUtil.cast(d);
			d2.removeListener(exId, listener);
		}

	}

	/**
	 * すべてのディスパッチャーおよび、リスナーを削除する。
	 */
	public void clear()
	{
		this.dispatchers.clear();
	}

	/**
	 * イベントを発火させる。<br>
	 */
	public void dispatch(EventObject e)
	{
		this.queue.add(e);
	}

	/**
	 * キューに存在するイベントを処理する。
	 */
	@Override
	public void update(float tpf) throws Exception
	{
		while(!this.queue.isEmpty())
		{
			EventObject e = this.queue.poll();
			this.invokeListener(tpf, e);
		}
	}

	/**
	 * イベントからディスパッチャーのリスナーを呼び出す。
	 */
	protected <E extends EventObject> void invokeListener(float tpf, E e)
	{
		Class<?> clazz = e.getClass();
		for(Dispatcher<?> d : this.dispatchers)
		{
			if(!d.getEventClass().isAssignableFrom(clazz))
			{
				continue;
			}

			Dispatcher<E> d2 = CastUtil.cast(d);
			d2.dispatch(tpf, e);
		}
	}


	public void register(Registerable r)
	{
		r.registerTo(this);
	}

	public void unregister(Registerable r)
	{
		r.unregisterTo(this);
	}

	/**
	 * イベント発火用のトリガーを生成する。
	 */
	public Trigger createTrigger()
	{
		return new TriggerImpl();
	}

	protected class TriggerImpl implements Trigger
	{

		protected TriggerImpl()
		{

		}

		public void dispatch(EventObject o)
		{
			EventManager.this.dispatch(o);
		}
	}

	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab0 = "\t".repeat(nest);

		String title = this.getClass().getSimpleName();

		sb.append(tab0);
		sb.append(title);
		sb.append(enter);

		for(Dispatcher<?> d : this.dispatchers)
		{
			d.createTreeText(sb, nest + 1);
		}


		return sb;
	}



}
