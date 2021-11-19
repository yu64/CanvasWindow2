package canvas2.core;

@FunctionalInterface
public interface Updatable extends AppObject{

	public void update(float tpf) throws Exception;


	public default void updateAndThrow(float tpf)
	{
		try
		{
			this.update(tpf);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
