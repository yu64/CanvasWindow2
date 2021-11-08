package canvas2.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import canvas2.debug.TextTree;
import canvas2.logic.Updatable;
import canvas2.util.CastUtil;

/**
 * イベントを管理する。<br>
 * (IDと)リスナーを登録すると、対応するイベントに紐づけられる。<br>
 * 発火したイベントは、キューに入れられる。<br>
 * イベントの発火のために更新する必要がある。<br>
 *
 */
public class EventManager implements Updatable, TextTree {

	private DispatcherMap dispatcher;
	private Set<EventRelay> relay;
	private Queue<EventObject> eventQueue = new ConcurrentLinkedQueue<>();

	public EventManager()
	{
		this(
				new DispatcherMap(),
				new HashSet<EventRelay>()
				);
	}

	public EventManager(DispatcherMap map, Set<EventRelay> relay)
	{
		this.dispatcher = map;
		this.relay = relay;
	}




	public void addDispatcher(Dispatcher<?, ?> d)
	{
		this.dispatcher.addDispatcher(d);
	}

	public void removeDispatcher(Dispatcher<?, ?> d)
	{
		this.dispatcher.removeDispatcher(d);
	}

	public void clearDispatcher()
	{
		this.dispatcher.removeAllDispatcher();
	}

	public <D extends Dispatcher<?, ?>> D getDispatcher(Class<D> clazz)
	{
		D obj = this.dispatcher.get(clazz);
		Objects.requireNonNull(obj, "Dispatcher not find ");

		return obj;
	}



	public void addRelay(EventRelay relay)
	{
		if(!relay.isColsed())
		{
			throw new RuntimeException("Already initialized");
		}

		this.relay.add(relay);
		relay.init(this);
	}

	public void removeRelay(EventRelay relay)
	{
		if(relay.isColsed())
		{
			throw new RuntimeException("Already closed");
		}

		relay.close();
		this.relay.remove(relay);
	}

	public void clearRelay()
	{
		for(EventRelay relay : this.relay)
		{
			this.removeRelay(relay);
		}
	}


	public <L extends EventListener> void add(L listener)
	{
		this.add((Object)null, listener);
	}

	public <L extends EventListener> void add(Object exId, L listener)
	{
		Class<L> clazz = CastUtil.getClass(listener);
		this.add(clazz, exId, listener);
	}

	public <L extends EventListener> void add(Class<L> listenerClass, L listener)
	{
		this.add(listenerClass, null, listener);
	}

	public <L extends EventListener> void add(Class<L> listenerClass, Object exId, L listener)
	{
		Class<L> clazz = listenerClass;

		this.dispatcher.forEachListener(clazz, d -> {

			d.addListener(exId, listener);
		});
	}



	public <L extends EventListener> void remove(L listener)
	{
		this.remove((Object)null, listener);
	}

	public <L extends EventListener> void remove(Object exId, L listener)
	{
		Class<L> clazz = CastUtil.getClass(listener);
		this.remove(clazz, exId, listener);
	}

	public <L extends EventListener> void remove(Class<L> listenerClass, L listener)
	{
		this.remove(listenerClass, null, listener);
	}

	public <L extends EventListener> void remove(Class<L> listenerClass, Object exId, L listener)
	{
		Class<L> clazz = listenerClass;

		this.dispatcher.forEachListener(clazz, d -> {

			d.removeListener(exId, listener);
		});
	}





	public void clearListener()
	{
		this.dispatcher.forEach(d -> {

			d.clearListener();
		});
	}





	public <E extends EventObject> void dispatch(E event)
	{
		this.eventQueue.add(event);
	}

	@Override
	public void update(float tpf) throws Exception
	{
		while(!this.eventQueue.isEmpty())
		{
			EventObject event = this.eventQueue.poll();
			this.invokeDispatcher(tpf, event);
		}
	}

	private <E extends EventObject> void invokeDispatcher(float tpf, E event)
	{
		Class<E> clazz = CastUtil.getClass(event);

		this.dispatcher.forEachEvent(clazz, d -> {

			d.dispatch(tpf, event);
		});
	}


	@Override
	public StringBuilder createTreeText(StringBuilder sb, int nest)
	{
		String enter = System.lineSeparator();
		String tab1 = "\t".repeat(nest);

		String title = this.getClass().getSimpleName();

		sb.append(tab1).append(title).append(enter);
		sb = this.dispatcher.createTreeText(sb, nest + 1);
		sb.append(enter);

		String tab2 = tab1 + "\t";
		String relayTitle = this.relay.getClass().toGenericString();
		sb.append(tab2).append(relayTitle).append(enter);

		String tab3 = tab2 + "\t";
		for(EventRelay relay : this.relay)
		{
			sb.append(tab3).append(relay).append(enter);
		}

		return sb;
	}







}
