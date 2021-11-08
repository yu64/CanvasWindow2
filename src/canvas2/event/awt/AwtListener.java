package canvas2.event.awt;

import java.awt.AWTEvent;
import java.util.EventListener;

/** float tpf, AWTEvent e **/
public interface AwtListener extends EventListener{


	public void action(float tpf, AWTEvent e) throws Exception;

}
