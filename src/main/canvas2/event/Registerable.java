package canvas2.event;

/**
 * 一つ以上のイベントのリスナーとして登録可能なもの
 */
public interface Registerable {

	public void registerTo(EventManager event);
	public void unregisterTo(EventManager event);
}
