package canvas2.event.flag;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import canvas2.core.event.Registerable;
import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.util.flag.FixedFlags;

/**
 * キーを監視するクラス。
 *
 */
public class KeyFlags extends FixedFlags<Integer>
	implements Registerable, AwtListener, KeyListener {



	/**
	 * キーの種類を指定して作成。
	 */
	public KeyFlags(Iterable<Integer> keys)
	{
		super(keys);
	}

	/**
	 * 指定されたキーが押されたか
	 */
	public boolean isPressed(Integer key)
	{
		return this.isTrue(key);
	}



	/**
	 * リスナー登録。
	 */
	@Override
	public void registerTo(EventManager event)
	{
		event.add(AWTEvent.class, KeyEvent.KEY_PRESSED, this);
		event.add(AWTEvent.class, KeyEvent.KEY_RELEASED, this);
	}

	/**
	 * リスナー削除。
	 */
	@Override
	public void unregisterTo(EventManager event)
	{
		event.remove(AWTEvent.class, KeyEvent.KEY_PRESSED, this);
		event.remove(AWTEvent.class, KeyEvent.KEY_RELEASED, this);
	}

	protected void setPressedFlag(KeyEvent e)
	{
		boolean isPressed = (e.getID() == KeyEvent.KEY_PRESSED);
		this.setFlag(e.getKeyCode(), isPressed, false);
	}

	/**
	 * キー入力を受け取るためのリスナー
	 */
	@Override
	public void act(float tpf, AWTEvent v) throws Exception
	{
		if( !(v instanceof KeyEvent))
		{
			return;
		}

		KeyEvent e = (KeyEvent) v;
		this.setPressedFlag(e);
	}




	/**
	 * リスナー登録。
	 */
	public void registerTo(Component c)
	{
		c.addKeyListener(this);
	}

	/**
	 * リスナー削除。
	 */
	public void unregisterTo(Component c)
	{
		c.removeKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		this.setPressedFlag(e);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		this.setPressedFlag(e);
	}


}
