package canvas2.event;

import java.util.EventObject;

import canvas2.core.debug.TextTree;
import canvas2.core.event.Listener;

/**
 * 指定された型のイベントをリスナーに振り分ける。
 */
public interface Dispatcher<E extends EventObject> extends TextTree{


	public Class<E> getEventClass();

	public void addListener(Object exId, Listener<? super E> listener);
	public void removeListener(Object exId, Listener<? super E> listener);
	public void clearListener();

	public void dispatch(float tpf, E event);


}
