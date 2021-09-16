package main.value;

import java.util.Set;

public interface Reader<D> {


	public Class<D> getDataClass();
	
	public void registerDependentTo(LoaderManager loader);

	public D read(LoaderManager loader, String path, Object op) throws Exception;
	
	public Set<ReadTicket<?>> getDependent(String path, Object op, int priority) throws Exception;
	
}
