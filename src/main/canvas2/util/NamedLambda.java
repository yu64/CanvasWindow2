package canvas2.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;



/**
 * ラムダ式に{@link Object#toString()}を実装させるユーティリティ
 * 
 */
public class NamedLambda {
	
	/**
	 * ラムダ式に名前を設定します。
	 */
	public static <T extends Serializable> T wrap(T obj)
	{
		try
		{
			Method m = obj.getClass().getDeclaredMethod("writeReplace");
			m.setAccessible(true);

			SerializedLambda info = (SerializedLambda) m.invoke(obj);
			String clazzName = info.getFunctionalInterfaceClass().replaceAll("/", ".");
			Class<T> clazz = CastUtil.cast(Class.forName(clazzName));
			
			return NamedLambda.wrap(
					info.getImplMethodName(), 
					clazz,
					obj
					);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ラムダ式に名前を設定します。
	 */
	@SafeVarargs
	public static <T> T wrap(String name, T obj, T... empty)
	{
		Class<T> clazz = CastUtil.getComponentClass(empty);
		return NamedLambda.wrap(name, clazz, obj);
	}
	
	/**
	 * ラムダ式に名前を設定します。
	 */
	public static <T> T wrap(String name, Class<T> clazz, T obj)
	{
		if(!clazz.isAnnotationPresent(FunctionalInterface.class))
		{
			throw new RuntimeException("It not is FunctionalInterface. " + clazz + " " + obj);
		}
		
		return CastUtil.cast(Proxy.newProxyInstance(
				NamedLambda.class.getClassLoader(), 
				new Class<?>[] {clazz}, 
				new Hander("Named[" + name + "] " + obj.toString(), obj)
				));
	}
	
	protected static class Hander implements InvocationHandler {

		private String name;
		private Object src;

		protected Hander(String name, Object src)
		{
			this.name = name;
			this.src = src;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			if(Object.class == method.getDeclaringClass())
			{
				return method.invoke(this, args);
			}
			
			return method.invoke(this.src, args);
		}
		
		@Override
		public String toString()
		{
			return this.name;
		}
		
	}
	
	
	
}
