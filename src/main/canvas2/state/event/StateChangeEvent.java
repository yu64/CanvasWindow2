package canvas2.state.event;

import java.util.EventObject;

import canvas2.state.StateTable;
import canvas2.state.obj.State;

public class StateChangeEvent extends EventObject{


	private State prev;
	private State next;
	private StateTable<?> src;
	private boolean isBefore;

	public StateChangeEvent(StateTable<?> source, State prev, State next, boolean isBefore)
	{
		super(source);
		this.src = source;
		this.prev = prev;
		this.next = next;
		this.isBefore = isBefore;
	}

	public StateTable<?> getTable()
	{
		return this.src;
	}

	public State getPrevState()
	{
		return this.prev;
	}

	public State getNextState()
	{
		return this.next;
	}

	public boolean isBefore()
	{
		return isBefore;
	}

}
