package canvas2.util;

/**
 *
 * 警告つきのキャストをするクラス。
 *
 */
public final class CastUtil {

	private CastUtil()
	{

	}

	@SuppressWarnings("unchecked")
	public static <T, O> T cast(O obj)
	{
		try
		{
			return (T) obj;
		}
		catch(Exception e)
		{
			String msg = "Invalid cast: " + obj.getClass();
			throw new RuntimeException(msg, e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T obj)
	{
		return (Class<T>) obj.getClass();
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] createArray(int i)
	{
		return (T[]) new Object[i];
	}
}
