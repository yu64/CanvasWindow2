package canvas2.state.event;

import java.util.EventObject;

import canvas2.state.StateTable;
import canvas2.state.obj.State;
import canvas2.util.CastUtil;

public class StateChangeEvent<S extends State> extends EventObject{


	private S prev;
	private S next;

	public StateChangeEvent(StateTable<S> source, S prev, S next)
	{
		super(source);
		this.prev = prev;
		this.next = next;
	}

	public StateTable<S> getTable()
	{
		return CastUtil.cast(this.getSource());
	}

	public S getPrevState()
	{
		return this.prev;
	}

	public S getNextState()
	{
		return this.next;
	}

}
