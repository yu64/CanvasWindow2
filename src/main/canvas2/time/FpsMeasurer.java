package canvas2.time;
import java.util.concurrent.TimeUnit;

import canvas2.util.TimeUtil;

/**
 * 現在の時間からFPS(frame per second)を求める。<br>
 * 
 */
public class FpsMeasurer implements Runnable{

	private final float ONE_SECOND;
	{
		this.ONE_SECOND = (float)TimeUtil.convert(
				1, 
				TimeUnit.SECONDS, 
				this.getUnit()
				);
	}
	
	private Clock clock;
	private long prev = 0L;
	private float fps = 1.0F;
	
	private long fixedUnitPerFrame;
	private boolean canFix = false;

	public FpsMeasurer()
	{
		this(Clock.getInstance());
	}

	public FpsMeasurer(Clock clock)
	{
		this.clock = clock;
		this.prev = clock.getTime(this.getUnit());
		
	}
	
	/**
	 * 内部で扱う時間の単位(精度)を取得。
	 */
	protected TimeUnit getUnit()
	{
		return TimeUnit.NANOSECONDS;
	}
	
	public void enableFixedFps(float framePerSecond)
	{
		this.fixedUnitPerFrame = (long) (this.ONE_SECOND / framePerSecond);
		this.canFix = true;
	}
	
	public void disableFixedFps()
	{
		this.canFix = false;
	}
	
	public final void update()
	{
		this.run();
	}

	@Override
	public void run()
	{
		long now = this.clock.getTime(this.getUnit());
		long unitPerFrame = now - this.prev;
		
		//n[時間単位]に1フレームが1秒にいくつ存在するか。
		float framePerSecond = (float)this.ONE_SECOND / unitPerFrame;

		//Fpsを設定。
		this.fps = framePerSecond;
		
		//更新時間を変更。
		this.prev = now;
		
		//Fpsを固定する場合、
		if(!this.canFix)
		{
			return;
		}
		
		long fixedUpf = this.fixedUnitPerFrame;
		long upf = unitPerFrame;
		
		//Npfが小さいと、Fpsは大きくなる。
		//固定Upfが小数であると仮定した場合、
		//現在のUpfの精度によって、この条件は、真にならない。
		if(upf < fixedUpf)
		{
			//差分、待機する。
			this.sleep(fixedUpf - upf);
		}
	}
	
	protected void sleep(long time)
	{
		try
		{
			this.getUnit().sleep(time);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public float getFps()
	{
		return this.fps;
	}


}
