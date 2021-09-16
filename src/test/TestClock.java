package test;

import java.util.concurrent.TimeUnit;

import main.time.Clock;

public class TestClock extends Clock{

	private long time = 0;

	public void addTime(long v)
	{
		this.time += v;
	}


	@Override
	public long getMillTime()
	{
		return this.getTime(TimeUnit.MILLISECONDS);
	}

	@Override
	public long getTime(TimeUnit unit)
	{
		return unit.convert(this.time, TimeUnit.MILLISECONDS);
	}

}
