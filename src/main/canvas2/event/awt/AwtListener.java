package canvas2.event.awt;

import java.awt.AWTEvent;

import canvas2.core.event.Listener;

/**
 * {@link AWTEvent}のリスナー<br>
 * 定義: (float tpf, {@link AWTEvent} awt)<br>
 *
 * 識別子にAWTイベントIDを指定できる。
 *
 *
 */
@FunctionalInterface
public interface AwtListener extends Listener<AWTEvent>{



}
