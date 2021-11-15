package canvas2.time;

import java.util.concurrent.TimeUnit;

import canvas2.core.Updatable;
import canvas2.logic.AppLogic;

/**
 * 現在の時間に基づいて、指定された処理を一定間隔で実行する。<br>
 * {@link AppLogic}で更新させる必要がある。
 */
public class TimeInterval extends Interval{

	private Clock clock;
	private long millTime;

	//最終実行時間
	private long prevTime = 0;

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
		super(action);
		this.clock = clock;
		this.millTime = ms;

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
		long time = this.clock.getMillTime();
		super.update(tpf);

		this.prevTime = time;
	}

	@Override
	protected double getElapsedTime(float tpf)
	{
		return this.clock.getMillTime() - this.prevTime;
	}

	@Override
	protected int getExecutionCount(float tpf, double elapsed)
	{
		return (int) (elapsed / this.millTime);
	}

	@Override
	protected double getCarryOver(float tpf, double elapsed, int count)
	{
		return elapsed - (this.millTime * count);
	}





}

