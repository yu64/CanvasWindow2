package canvas2.time;

import java.util.concurrent.TimeUnit;

import canvas2.logic.Updatable;

/**
 * 現在の時間に基づいて、指定された処理を一定間隔で実行する。
 */
public class TimeInterval implements Updatable{

	private Clock clock;
	private long millTime;
	private Updatable action;

	private long prevTime = 0;
	private long unusedTotalTime = 0;

	public TimeInterval(long time, Updatable action)
	{
		this(Clock.getInstance(), time, TimeUnit.MILLISECONDS, action);
	}

	public TimeInterval(long time, TimeUnit unit, Updatable action)
	{
		this(Clock.getInstance(), time, unit, action);
	}

	public TimeInterval(Clock clock, long time, TimeUnit unit, Updatable action)
	{
		this.clock = clock;
		this.millTime = TimeUnit.MILLISECONDS.convert(time, unit);
		this.action = action;

		this.prevTime = clock.getMillTime();
	}

	@Override
	public void update(float tpf)
	{
		long mill = this.clock.getMillTime();
		long diff = mill - this.prevTime;

		long totalTime = this.unusedTotalTime + diff;

		long count = totalTime / this.millTime;
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

		this.unusedTotalTime = totalTime - this.millTime * count;
		this.prevTime = mill;
	}




}

