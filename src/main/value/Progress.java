package main.value;

public class Progress {

	private long maxProgress;
	private long progress;

	public Progress(long maxProgress)
	{
		this.maxProgress = maxProgress;
	}
	
	public void add(long progress)
	{
		this.progress += progress;
	}
	
	public void set(long progress)
	{
		this.progress = progress;
	}
	
	public long getMaxProgress()
	{
		return this.maxProgress;
	}
	
	public long getProgress() 
	{
		return this.progress;
	}
}
