package canvas2.core;

/**
 * 更新可能なもの。関数型インターフェイスとして使える。<br>
 */
@FunctionalInterface
public interface Updatable extends AppObject {

	public void update(float tpf) throws Exception;


	public default void updateOrThrow(float tpf)
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
