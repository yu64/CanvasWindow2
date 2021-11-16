package canvas2.event.awt;

import java.awt.AWTEvent;

import canvas2.event.Listener;

/** float tpf, AWTEvent e **/
public interface AwtListener extends Listener<AWTEvent>{


	public void act(float tpf, AWTEvent e) throws Exception;




}
