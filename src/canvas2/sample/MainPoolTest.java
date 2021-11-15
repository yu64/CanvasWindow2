package canvas2.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import canvas2.util.Pool;

public class MainPoolTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		Pool pool = new Pool();
		pool.register(
				Heavy.class,
				Heavy::new,
				v -> {},
				0,
				10
				);

		pool.register(
				ArrayList.class,
				ArrayList::new,
				v -> v.clear(),
				1,
				1
				);


		@SuppressWarnings("rawtypes")
		ArrayList list = pool.obtain(ArrayList.class);
		list.add(new Object());
		list.add(new Object());

		pool.free(list);



		long startTime = System.nanoTime();

		System.out.println("get " + new Heavy());
		System.out.println(System.nanoTime() - startTime);

		startTime = System.nanoTime();



		Heavy obj = pool.obtain(Heavy.class);
		System.out.println("get " + obj);
		pool.free(obj);

		System.out.println(System.nanoTime() - startTime);



		startTime = System.nanoTime();

		obj = pool.obtain(Heavy.class);
		System.out.println("get " + obj);
		pool.free(obj);

		System.out.println(System.nanoTime() - startTime);



		startTime = System.nanoTime();

		obj = pool.obtain(Heavy.class);
		System.out.println("get " + obj);
		pool.free(obj);

		System.out.println(System.nanoTime() - startTime);

	}

	private static class Heavy {

		private Map<Object, Object> temp1 = new HashMap<>();
		private List<Object> temp2 = new LinkedList<>();

		public Heavy()
		{
			try
			{
				TimeUnit.MICROSECONDS.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
