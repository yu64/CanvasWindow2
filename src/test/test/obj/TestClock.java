package test.obj;

import java.util.concurrent.TimeUnit;

import canvas2.time.Clock;

public class TestClock extends Clock{

	private long time = 0;

	public void addTime(long ms)
	{
		this.time += ms;
	}

	@Override
	public void sleep(long time, TimeUnit unit) throws InterruptedException
	{
		this.time += unit.toMillis(time);
	}

	@Override
	public long getTime(TimeUnit unit)
	{
		return unit.convert(this.time, TimeUnit.MILLISECONDS);
	}

}
