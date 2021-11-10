package canvas2.time;

import java.util.concurrent.TimeUnit;

import canvas2.logic.AppLogic;
import canvas2.logic.Updatable;

/**
 * 現在の時間に基づいて、指定された処理を一定間隔で実行する。<br>
 * {@link AppLogic}で更新させる必要がある。
 */
public class TimeInterval implements Updatable{

	private Clock clock;
	private long millTime;
	private Updatable action;

	//最終実行時間
	private long prevTime = 0;

	//経過時間のうち使われていない時間(次回に持ち越す時間)
	private long unusedTotalTime = 0;



	public TimeInterval(long ms, Updatable action)
	{
		this(Clock.getInstance(), ms, action);
	}

	public TimeInterval(long time, TimeUnit unit, Updatable action)
	{
		this(Clock.getInstance(), unit.toMillis(time), action);
	}

	public TimeInterval(Clock clock, long ms, Updatable action)
	{
		this.clock = clock;
		this.millTime = ms;
		this.action = action;

		this.prevTime = clock.getMillTime();
	}

	/**
	 * インターバルを設定します。
	 */
	public void setIntervalTime(long time, TimeUnit unit)
	{
		this.setIntervalTime(unit.toMillis(time));
	}

	/**
	 * インターバルを設定します。
	 */
	public void setIntervalTime(long ms)
	{
		this.millTime = ms;
	}



	@Override
	public void update(float tpf)
	{
		//経過時間を求める。
		long mill = this.clock.getMillTime();
		long diff = mill - this.prevTime;

		//持ち越し分を考慮し、合計待機時間を求める。。
		long totalTime = this.unusedTotalTime + diff;

		//待機時間をもとに、実行できる回数、処理を実行する。
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

		//余った待機時間を持ち越す。
		this.unusedTotalTime = totalTime - this.millTime * count;

		//経過時間を求めるために、最終実行時間を変更。
		this.prevTime = mill;
	}




}

