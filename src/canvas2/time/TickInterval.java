package canvas2.time;

import canvas2.logic.AppLogic;
import canvas2.logic.Updatable;

/**
 * tickの値に基づいて、指定された処理を一定間隔で実行する。<br>
 * {@link AppLogic}で更新させる必要がある。
 */
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

