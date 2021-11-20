package canvas2.time;

import java.util.concurrent.TimeUnit;

import canvas2.util.TimeUtil;

/**
 * 指定したtickと、現在の時間からTPF(tick per frame)を求める。<br>
 * <br>
 * 
 * TPF未使用であるとき、移動量は、1フレームに依存するため、<br>
 * FPSが変化した場合、現実の時間内(例えば、1秒間)の移動量も変化する。<br>
 * <br>
 * 
 * TPFは、1frameが何tickであるかを意味する。<br>
 * TPFと1tick単位での変化量をかけることで、<br>
 * FPSの変動を気にせず、現実の時間内で一定の変化を起こせる。
 */
public class TpfMeasurer implements Runnable{

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
	private float tickPerUnit;
	private float tpf = 1.0F;

	public TpfMeasurer(float tickPerSecond)
	{
		this(Clock.getInstance(), tickPerSecond);
	}

	public TpfMeasurer(Clock clock, float tickPerSecond)
	{
		this.clock = clock;
		this.prev = clock.getTime(this.getUnit());
		this.setTarget(tickPerSecond);
	}
	
	/**
	 * 内部で扱う時間の単位(精度)を取得。
	 */
	protected TimeUnit getUnit()
	{
		return TimeUnit.MILLISECONDS;
	}
	
	
	public void setTarget(float tickPerSecond)
	{
		this.tickPerUnit = tickPerSecond / this.ONE_SECOND;
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
		float tickPerFrame = this.tickPerUnit * unitPerFrame;
		
		this.tpf = tickPerFrame;
		this.prev = now;
	}

	public float getTpf()
	{
		return this.tpf;
	}

	


}
