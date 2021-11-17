package canvas2.state.event;

import canvas2.core.Trigger;
import canvas2.event.EventManager;
import canvas2.state.StateTable;
import canvas2.state.obj.State;

public class TriggerStateTable<S extends State> extends StateTable<S>{

	private Trigger trigger;

	public TriggerStateTable(S initState, EventManager event)
	{
		this(initState, event.createTrigger());
	}

	public TriggerStateTable(S initState, Trigger trigger)
	{
		super(initState);
		this.trigger = trigger;
	}

	@Override
	protected void doTransferAction(float tpf, S ori, S dest)
	{
		this.trigger.dispatch(new StateChangeEvent<S>(this, ori, dest));
		super.doTransferAction(tpf, ori, dest);
	}




}
