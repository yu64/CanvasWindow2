package canvas2.core.event;

import java.util.EventListener;
import java.util.EventObject;

public interface Listener<E extends EventObject> extends EventListener{

	public void act(float tpf, E e) throws Exception;

	public default void actAndThrow(float tpf, E e)
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
