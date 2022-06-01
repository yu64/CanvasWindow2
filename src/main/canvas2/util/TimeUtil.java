package canvas2.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

	/**
	 * 小数に対応させた{@link TimeUnit#convert(long, TimeUnit)}
	 */
	public static double convert(double time, TimeUnit from, TimeUnit to) 
	{
	    if(from == to) 
	    {
	        return time;
	    }
	    
	    if(from.ordinal() < to.ordinal()) 
	    {
	        return time / from.convert(1, to);
	    } 
	    else 
	    {
	        return time * to.convert(1, from);
	    }
	}
	
}
