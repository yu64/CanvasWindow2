package main.time;

import main.logic.Updatable;

public class TickInterval implements Updatable{

	private long tick;
	private Updatable action;

	private float unusedTotalTick = 0.0F;

	public TickInterval(long tick, Updatable action)
	{
		this.tick = tick;
		this.action = action;

	}


	@Override
	public void update(float tpf)
	{
		float totalTick = this.unusedTotalTick + tpf;
		int count = (int) (totalTick / this.tick);

		for(int i = 0; i < count; i++)
		{
			try
			{
				this.action.update(tpf);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		this.unusedTotalTick = totalTick - this.tick * count;
	}




}

