package main.time;

import main.logic.AppLogic;

/**
 * 指定したtickと、現在の時間からTpf(tick per frame)を求める。<br>
 * {@link AppLogic}がこれを用いている。
 *
 */
public class TpfMeasurer {

	private Clock clock;
	private float targetTickPerMill;
	private long prevMill = 0L;
	private float tpf = 1.0F;

	public TpfMeasurer(float tickPerSecond)
	{
		this(Clock.getInstance(), tickPerSecond);
	}

	public TpfMeasurer(Clock clock, float tickPerSecond)
	{
		this.clock = clock;
		this.targetTickPerMill = tickPerSecond / 1000.0F;
		this.prevMill = clock.getMillTime();
	}


	public void update()
	{
		long nowMill = this.clock.getMillTime();

		long millPerFrame = nowMill - this.prevMill;
		float tickPerFrame = this.targetTickPerMill * millPerFrame;


		this.tpf = tickPerFrame;

		this.prevMill = nowMill;
	}

	public float getTpf()
	{
		return this.tpf;
	}


}
