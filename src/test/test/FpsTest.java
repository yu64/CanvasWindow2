package test;

import java.util.Random;
import java.util.function.LongSupplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import canvas2.time.FpsMeasurer;
import test.obj.TestClock;

public class FpsTest {

	
	
	
	@Test
	public void test()
	{
		Random r = new Random();
		
		//1フレームが1000ms
		this.assertRange(1.0F, this.run("1000msPerFrame", () -> 1000));
		
		//1フレームが500ms
		this.assertRange(2.0F, this.run("500msPerFrame", () -> 500));
		
		//1フレームが250ms
		this.assertRange(4.0F, this.run("250msPerFrame", () -> 250));
				
		//1フレームが16から17ms
		this.assertRange(58.0F, 62.0F, this.run("16_17msPerFrame", () -> 16 + r.nextInt(2)));
		
		
		//2.0fpsに固定されていて、1フレームが1000ms
		this.assertRange(1.0F, this.run("1000msPerFrame_Fixed2.0Fps", 2.0F, () -> 1000));
		
		//2.0fpsに固定されていて、1フレームが500ms
		this.assertRange(1.8F, 2.2F, this.run("500msPerFrame_Fixed2.0Fps", 2.0F, () -> 250));
		
		//2.0fpsに固定されていて、1フレームが250ms
		this.assertRange(1.8F, 2.2F, this.run("250msPerFrame_Fixed2.0Fps", 2.0F, () -> 250));
		
		
		//30.0fpsに固定されていて、1フレームが16から17ms
		this.assertRange(28.0F, 32.0F, this.run("16_17msPerFrame_Fixed30.0Fps", 30.0F, () -> 16 + r.nextInt(2)));
		
		//30.0fpsに固定されていて、1フレームが16から17ms
		this.assertRange(8.0F, 12.0F, this.run("16_17msPerFrame_Fixed10.0Fps", 10.0F, () -> 16 + r.nextInt(2)));
		
		
		//1フレームが100ms
		this.assertRange(10.0F, this.run("5msPerFrame", () -> 100));
				
		//1フレームが10ms
		this.assertRange(100.0F, this.run("10msPerFrame", () -> 10));
		
		//1フレームが5ms
		this.assertRange(200.0F, this.run("5msPerFrame", () -> 5));
		
		//100.0fpsに固定されていて、1フレームが5ms
		this.assertRange(98.0F, 102.0F, this.run("5msPerFrame_Fixed100.0Fps", 100.0F, () -> 5));
		
		//100.0fpsに固定されていて、1フレームが2から10ms
		this.assertRange(98.0F, 102.0F, this.run("1_10msPerFrame_Fixed100.0Fps", 100.0F, () -> 1 + r.nextInt(11) ));
		
		//100.0fpsに固定されていて、1フレームが1から100ms
		this.assertRange(12.0F, 102.0F, this.run("1_100msPerFrame_Fixed100.0Fps", 100.0F, () -> 1 + r.nextInt(101) ));
		
	}
	
	
	public void assertRange(float value, float x)
	{
		this.assertRange(value, value, x);
	}
	
	public void assertRange(float min, float max, float x)
	{
		Assertions.assertTrue(min <= x && x <= max, "not " + min + " <= " + x + " <= " + max);
	}
	
	public float run(String name, LongSupplier time)
	{
		TestClock clock = new TestClock();
		FpsMeasurer measurer = new FpsMeasurer(clock);
		return this.run(name, clock, measurer, time);
	}
	
	public float run(String name, float fixed, LongSupplier time)
	{
		TestClock clock = new TestClock();
		FpsMeasurer measurer = new FpsMeasurer(clock);
		measurer.enableFixedFps(fixed);
		return this.run(name, clock, measurer, time);
	}
	
	public float run(String n, TestClock c, FpsMeasurer m, LongSupplier t)
	{
		
		float total = 0.0F;
		float max = 0.0F;
		float min = Float.MAX_VALUE;
		
		for(int i = 0; i < 100; i++)
		{
			c.addTime(t.getAsLong());
			
			m.update();
			float fps = m.getFps();
			total += fps;
			
			max = (max < fps ? fps : max);
			min = (fps < min ? fps : min);
		}

		float result = total / 100;
		
		System.out.println(n + " " + result + " " + max + " " + min);
	
		return result;
	}
	
}
