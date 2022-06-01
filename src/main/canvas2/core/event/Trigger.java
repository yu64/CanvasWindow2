package canvas2.core.event;

import java.util.EventObject;

/**
 * 任意のイベントを発火させる機能
 */
@FunctionalInterface
public interface Trigger {

	public static Trigger createEmpty()
	{
		return new Trigger() {

			@Override
			public void dispatch(EventObject o)
			{
			}
		};
	}
	
	
	
	public void dispatch(EventObject o);
}
