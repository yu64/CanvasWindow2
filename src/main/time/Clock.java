package main.time;

import java.util.concurrent.TimeUnit;

/**
 * 時間を取得するオブジェクト。<br>
 * 独自の時間を使用したいときは、継承すること。
 *
 */
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
