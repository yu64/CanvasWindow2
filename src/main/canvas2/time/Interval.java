package canvas2.time;

import canvas2.core.Updatable;

public abstract class Interval implements Updatable{

	private Updatable action;
	private boolean isPause = false;
	private double carryOver = 0;

	public Interval(Updatable action)
	{
		this.action = action;
	}

	public Updatable getAction()
	{
		return this.action;
	}

	public void enablePause()
	{
		this.isPause = true;
	}

	public void disablePause()
	{
		this.isPause = false;
	}

	public boolean isPause()
	{
		return this.isPause;
	}

	@Override
	public void update(float tpf)
	{
		if(this.isPause())
		{
			return;
		}

		//持ち越し分を考慮し、経過時間を求める。
		double elapsed = this.carryOver + this.getElapsedTime(tpf);

		//待機時間をもとに、実行できる回数、処理を実行する。
		int count = this.getExecutionCount(tpf, elapsed);
		this.doAction(tpf, count);

		//持ち越し分を求める。
		this.carryOver = this.getCarryOver(tpf, elapsed, count);

	}

	protected void doAction(float tpf, int count)
	{
		for(int i = 0; i < count; i++)
		{
			try
			{
				this.getAction().update(tpf);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	protected abstract double getElapsedTime(float tpf);
	protected abstract int getExecutionCount(float tpf, double elapsed);
	protected abstract double getCarryOver(float tpf, double elapsed, int count);

}
