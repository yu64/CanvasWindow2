package canvas2.state.event;

import java.util.EventObject;

import canvas2.state.StateTable;
import canvas2.state.obj.State;
import canvas2.util.CastUtil;

public class StateChangeEvent extends EventObject{


	private State prev;
	private State next;

	public StateChangeEvent(StateTable<?> source, State prev, State next)
	{
		super(source);
		this.prev = prev;
		this.next = next;
	}

	public StateTable<?> getTable()
	{
		return CastUtil.cast(this.getSource());
	}

	public State getPrevState()
	{
		return this.prev;
	}

	public State getNextState()
	{
		return this.next;
	}

}
