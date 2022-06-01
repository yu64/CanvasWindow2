package canvas2.core.event;

import java.util.EventListener;
import java.util.EventObject;

/**
 * イベントによって呼び出されるリスナー
 */
@FunctionalInterface
public interface Listener<E extends EventObject> extends EventListener{

	/**
	 * イベントによって呼び出されるメソッド
	 */
	public void act(float tpf, E e) throws Exception;

	/**
	 * 例外を非検査例外に包んで投げるメソッド。
	 * @see {@link Listener#act(float, EventObject)}
	 */
	public default void actOrThrow(float tpf, E e)
	{
		try
		{
			this.act(tpf, e);
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
