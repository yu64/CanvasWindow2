package canvas2.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 * 同じクラスのインスタンスをまとめ、一つとして扱うクラス。
 */
public class ListenerHub<T> {



	private Collection<T> listeners;
	private T handler;

	@SuppressWarnings("unchecked")
	public ListenerHub(Class<T> listenersClass, Collection<T> listeners)
	{
		this.listeners = listeners;

		this.handler = (T) Proxy.newProxyInstance(
				ListenerHub.class.getClassLoader(),
				new Class[] {listenersClass},
				this.createHandler()
				);
	}

	protected Handler createHandler()
	{
		return new Handler();
	}

	public T getHandler()
	{
		return handler;
	}

	public Collection<T> getListeners()
	{
		return this.listeners;
	}

	protected class Handler implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			if(Object.class == method.getDeclaringClass())
			{
				return method.invoke(this, args);
			}


			for(T h : ListenerHub.this.listeners)
			{
				if(h == null)
				{
					continue;
				}

				method.invoke(h, args);
			}

			return null;
		}


		@Override
		public int hashCode()
		{
			return System.identityHashCode(this);
		}

		@Override
		public boolean equals(Object obj)
		{
			return this == obj;
		}

		@Override
		public String toString()
		{
			return this.getClass().getName() + '@' +
		            Integer.toHexString(this.hashCode());
		}

	}

}
