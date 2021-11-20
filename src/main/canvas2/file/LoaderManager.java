package canvas2.file;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import canvas2.core.Updatable;
import canvas2.util.CastUtil;

@Deprecated
public class LoaderManager implements Updatable{

	//登録されたReader
	private Map<Class<?>, Reader<?>> reader = new HashMap<>();

	
	private final Comparator<ReadTicket<?>> order = 
			Comparator.comparing((ReadTicket<?> t) -> t.getPriority()).reversed();
	
	private Queue<ReadTicket<?>> readQueue = new PriorityBlockingQueue<>(11, this.order);
	private Map<Class<?>, Map<String, ReadTicket<?>>> ticketCache = new HashMap<>();
	
	
	public LoaderManager()
	{
		
	}

	public void register(Reader<?> r)
	{
		this.reader.put(r.getDataClass(), r);
		r.registerDependentTo(this);
	}
	
	public <D> Reader<D> getReader(Class<D> data)
	{
		return CastUtil.cast(this.reader.get(data));
	}
	
	public <D> boolean hasReader(Class<D> data)
	{
		return (null != this.reader.get(data));
	}
	
	public <D> ReadTicket<D> load(Class<D> dataType, String path)
	{
		ReadTicket<D> cache = this.getCache(dataType, path);
		if(cache != null)
		{
			return cache;
		}
		
		return this.load(dataType, path, null);
	}
	
	public <D> ReadTicket<D> load(Class<D> dataType, String path, Object op)
	{
		ReadTicket<D> cache = this.getCache(dataType, path);
		if(cache != null)
		{
			return cache;
		}
		
		return this.load(new ReadTicket<D>(dataType, path, op));
	}
	
	public <D> ReadTicket<D> load(ReadTicket<D> ticket)
	{
		Objects.requireNonNull(ticket);
		
		ReadTicket<D> cache = this.getCache(ticket);
		if(cache != null)
		{
			ticket.setResult(cache.getResult());
			return cache;
		}
		
		if(this.readQueue.contains(ticket))
		{
			throw new RuntimeException("");
		}
		
		Reader<D> reader = this.getReader(ticket.getDataClass());
		if(reader == null)
		{
			throw new RuntimeException("not found reader: " + ticket.getDataClass());
		}

		Set<ReadTicket<?>> depTicket = null;
		
		try
		{
			depTicket = reader.getDependent(ticket.getPath(), ticket.getOption(), 1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(depTicket != null)
		{
			for(ReadTicket<?> t : depTicket)
			{
				if(t == null)
				{
					continue;
				}
				
				this.load(t);
			}
		}
		
		this.readQueue.add(ticket);

		return ticket;
	}
	
	
	
	
	
	@Override
	public void update(float tpf) throws Exception
	{
		if(this.readQueue.isEmpty())
		{
			return;
		}
		
		ReadTicket<?> ticket = this.readQueue.poll();
		
		ReadTicket<?> cache = this.getCache(ticket);
		if(cache != null)
		{
			ticket.setResult(CastUtil.cast(cache.getResult()));
			return;
		}
		
		
		
		Reader<?> reader = this.getReader(ticket.getDataClass());
		Object data = reader.read(this, ticket.getPath(), ticket.getOption());
		ticket.setResult(CastUtil.cast(data));
		
		
		this.addCache(ticket);
		
	}

	protected void addCache(ReadTicket<?> ticket)
	{
		Map<String, ReadTicket<?>> map = this.ticketCache.get(ticket.getDataClass());
		if(map == null)
		{
			map = new HashMap<>();
		}
		
		map.put(ticket.getPath(), ticket);
		
		this.ticketCache.put(ticket.getDataClass(), map);
	}
	
	public <D> ReadTicket<D> get(ReadTicket<D> ticket)
	{
		return this.getCache(ticket);
	}
	
	public <D> ReadTicket<D> get(Class<D> dataType, String path)
	{
		return this.getCache(dataType, path);
	}
	
	protected <D> ReadTicket<D> getCache(ReadTicket<D> ticket)
	{
		Objects.requireNonNull(ticket);
		
		return this.getCache(ticket.getDataClass(), ticket.getPath());
	}
	
	protected <D> ReadTicket<D> getCache(Class<D> dataType, String path)
	{
		
		Map<String, ReadTicket<?>> map = this.ticketCache.get(dataType);
		if(map == null)
		{
			return null;
		}
		
		return CastUtil.cast(map.get(path));
	}

	public int getSize()
	{
		return this.readQueue.size();
	}

}
