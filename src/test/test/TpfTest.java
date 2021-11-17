package test;

import java.util.function.LongSupplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import canvas2.time.TpfMeasurer;

class TpfTest {

	@Test
	void test()
	{
		System.out.println(this.getClass().getSimpleName());

		//1フレームが1msのとき
		int countA = TpfTest.runLoop(() -> 1);

		//1フレームが2msのとき
		int countB = TpfTest.runLoop(() -> 2);

		//1フレームが1msから10msでランダムなとき
		int countC = TpfTest.runLoop(() -> (long)(Math.random() * 10 + 1));
		
		//1フレームが0msから10msでランダムなとき
		int countD = TpfTest.runLoop(() -> (long)(Math.random() * 11));

		System.out.println("A: " + countA);
		System.out.println("B: " + countB);
		System.out.println("C: " + countC);
		System.out.println("D: " + countD);

		Assertions.assertEquals(countA, countB);
		Assertions.assertEquals(countA, countC);
		Assertions.assertEquals(countA, countD);
	}


	public static int runLoop(LongSupplier millPerFrame)
	{

		TestClock clock = new TestClock();
		TpfMeasurer tpf = new TpfMeasurer(clock, 100.0F);

		int count = 0;

		//予定実行時間
		int remaingTime = 1000;

		while(remaingTime > 0)
		{
			long usedTime = millPerFrame.getAsLong();

			//残り実行時間を超えるときは、残り実行時間を使用。
			if(remaingTime < usedTime)
			{
				usedTime = remaingTime;
			}

			//処理の経過時間を追加
			clock.addTime(usedTime);
			remaingTime += -usedTime;

			tpf.update();
			count += Math.round(1000.0F * tpf.getTpf());
		}


		return count;
	}



}
