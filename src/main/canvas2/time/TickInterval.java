package canvas2.time;

import canvas2.core.Updatable;
import canvas2.logic.AppLogic;

/**
 * tickの値に基づいて、指定された処理を一定間隔で実行する。<br>
 * {@link AppLogic}で更新させる必要がある。
 */
public class TickInterval extends Interval{

	private long tick;

	public TickInterval(long tick, Updatable action)
	{
		super(action);
		this.tick = tick;

	}

	@Override
	protected double getElapsedTime(float tpf)
	{
		return tpf;
	}


	@Override
	protected int getExecutionCount(float tpf, double elapsed)
	{
		return (int) (elapsed / this.tick);
	}


	@Override
	protected double getCarryOver(float tpf, double elapsed, int count)
	{
		return elapsed - (this.tick * count);
	}





}

