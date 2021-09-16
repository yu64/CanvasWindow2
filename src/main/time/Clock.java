package main.time;

import java.util.concurrent.TimeUnit;

public class Clock {

	private static Clock INSTANCE = new Clock();

	public static Clock getInstance()
	{
		if(Clock.INSTANCE == null)
		{
			Clock.INSTANCE = new Clock();
		}

		return Clock.INSTANCE;
	}

	public long getMillTime()
	{
		return this.getTime(TimeUnit.MILLISECONDS);
	}


	public long getTime(TimeUnit unit)
	{
		return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}



}
