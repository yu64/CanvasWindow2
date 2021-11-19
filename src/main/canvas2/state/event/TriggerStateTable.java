package canvas2.state.event;

import canvas2.core.event.Trigger;
import canvas2.state.StateTable;
import canvas2.state.obj.State;

public class TriggerStateTable<S extends State> extends StateTable<S>{

	private Trigger trigger = null;

	public TriggerStateTable(S initState)
	{
		super(initState);
	}

	public TriggerStateTable(S initState, Trigger trigger)
	{
		this(initState);
		this.trigger = trigger;
	}

	@Override
	protected void onPostChange(S prev, S now)
	{
		if(this.trigger != null)
		{
			this.trigger.dispatch(new StateChangeEvent(this, prev, now));
		}

	}




}
