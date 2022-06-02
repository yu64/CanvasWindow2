package canvas2.time;

import java.util.concurrent.TimeUnit;

/**
 * 時間の取得、待機をするオブジェクト。<br>
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
	
	public long getSecondTime()
	{
		return this.getTime(TimeUnit.SECONDS);
	}
	
	public long getMillTime()
	{
		return this.getTime(TimeUnit.MILLISECONDS);
	}
	
	public long getNanoTime()
	{
		return this.getTime(TimeUnit.NANOSECONDS);
	}

	public long getTime(TimeUnit unit)
	{
		return unit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	public void sleepNano(long ns) throws InterruptedException
	{
		this.sleep(ns, TimeUnit.NANOSECONDS);
	}
	
	public void sleep(long time, TimeUnit unit) throws InterruptedException
	{
		long ns = TimeUnit.NANOSECONDS.convert(time, unit);
		
		if(ns <= 0)
		{
			return;
		}

		//ビジーウェイトによる待機は、負荷が大きいため、
		//最後の一定期間以外は、普通のsleepにする。

		//待機終了時刻
		long endTime = this.getNanoTime() + ns;

		//この時点で必要な待機時間
		long waitTime = endTime - this.getNanoTime();
		if(waitTime <= 0)
		{
			return;
		}

		//ビジーウェイトに使うための時間
		long busyTime = 50_000_000;

		//ビジーウェイト以外の時間がある場合は、sleep
		if(waitTime > busyTime)
		{
			TimeUnit.NANOSECONDS.sleep(waitTime - busyTime);
		}

		//ビジーウェイト
		//開始時刻から、終了時刻までループ。
		while(this.getNanoTime() < endTime);
	}

}
