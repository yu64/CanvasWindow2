package main.value;

public class ReadTicket<D> {

	private Class<D> data;
	private String path;
	private Object op;
	private int priority;
	
	private D result;

	public ReadTicket(Class<D> data, String path)
	{
		this(data, path, null, 0);
	}
	
	public ReadTicket(Class<D> data, String path, Object op)
	{
		this(data, path, op, 0);
	}
	
	public ReadTicket(Class<D> data, String path, Object op, int priority)
	{
		this.data = data;
		this.path = path;
		this.op = op;
		this.priority = priority;
	}
	
	
	public void setResult(D data)
	{
		this.result = data;
	}
	
	public D getResult()
	{
		return this.result;
	}
	
	
	
	public Class<D> getDataClass()
	{
		return this.data;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public Object getOption()
	{
		return this.op;
	}
	
	public int getPriority()
	{
		return this.priority;
	}
}
