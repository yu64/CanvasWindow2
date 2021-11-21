package canvas2.core.event;

import java.util.EventObject;

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
