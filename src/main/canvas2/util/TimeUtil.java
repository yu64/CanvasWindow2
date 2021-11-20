package canvas2.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

	
	public static double convert(double time, TimeUnit from, TimeUnit to) 
	{
	    if (from == to) 
	    {
	        return time;
	    }
	    
	    if (from.ordinal() < to.ordinal()) 
	    {
	        return time / from.convert(1, to);
	    } 
	    else 
	    {
	        return time * to.convert(1, from);
	    }
	}
	
}
