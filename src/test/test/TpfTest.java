package test;

import java.util.function.LongSupplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import canvas2.time.TpfMeasurer;
import test.obj.TestClock;

class TpfTest {

	@Test
	void test()
	{
		System.out.println("[" + this.getClass().getSimpleName() + "] setup");

		//1フレームが1msのとき
		float countA = TpfTest.runLoop(() -> 1);

		//1フレームが2msのとき
		float countB = TpfTest.runLoop(() -> 2);

		//1フレームが1msから10msでランダムなとき
		float countC = TpfTest.runLoop(() -> (long)(Math.random() * 10 + 1));
		
		//1フレームが0msから10msでランダムなとき
		float countD = TpfTest.runLoop(() -> (long)(Math.random() * 11));

		//1フレームが1msから1000msでランダムなとき
		float countE = TpfTest.runLoop(() -> (long)(Math.random() * 1000 + 1));

				
		System.out.println("A: " + countA);
		System.out.println("B: " + countB);
		System.out.println("C: " + countC);
		System.out.println("D: " + countD);
		System.out.println("E: " + countE);

		Assertions.assertEquals(countA, countB);
		Assertions.assertEquals(countA, countC);
		Assertions.assertEquals(countA, countD);
		Assertions.assertEquals(countA, countE);
		
		Assertions.assertNotEquals(countA, 0);
	}


	public static float runLoop(LongSupplier millPerFrame)
	{
		float tick = 100.0F;
		
		TestClock clock = new TestClock();
		TpfMeasurer tpf = new TpfMeasurer(clock, tick);

		
		float pos = 0;
		
		//1Tickの移動量
		float speed = 1000.0F;
		

		//予定実行時間
		int remaingTime = 1000;

		while(remaingTime > 0)
		{
			long elapsed = millPerFrame.getAsLong();

			//残り実行時間を超えるときは、残り実行時間を使用。
			if(remaingTime < elapsed)
			{
				elapsed = remaingTime;
			}

			//処理の経過時間を追加
			clock.addTime(elapsed);
			remaingTime += -elapsed;

			tpf.update();
			pos += speed * tpf.getTpf();
		}


		return pos;
	}
	


}
