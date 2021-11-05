package main.util;

/**
 *
 * 警告つきのキャストをするクラス。
 *
 */
public class CastUtil {

	private CastUtil()
	{

	}

	@SuppressWarnings("unchecked")
	public static <T, O> T cast(O obj)
	{
		return (T) obj;
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
