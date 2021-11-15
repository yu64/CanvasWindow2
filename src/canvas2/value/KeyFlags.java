package canvas2.value;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

import canvas2.event.EventManager;
import canvas2.event.awt.AwtListener;
import canvas2.util.flag.BasicFlags;

/**
 * キーを監視するクラス。
 *
 */
public class KeyFlags extends BasicFlags<Integer> implements AwtListener{




	/**
	 * キーの種類を指定して作成。
	 */
	@SafeVarargs
	public KeyFlags(Integer... keys)
	{
		super(keys);
	}

	/**
	 * キーの種類を指定して作成。
	 */
	public KeyFlags(Set<Integer> keys)
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
	public void registerTo(EventManager event)
	{
		event.add(AwtListener.class, KeyEvent.KEY_PRESSED, this);
		event.add(AwtListener.class, KeyEvent.KEY_RELEASED, this);
	}

	/**
	 * リスナー削除。
	 */
	public void unregisterTo(EventManager event)
	{
		event.remove(AwtListener.class, KeyEvent.KEY_PRESSED, this);
		event.remove(AwtListener.class, KeyEvent.KEY_RELEASED, this);
	}

	/**
	 * キー入力を受け取るためのリスナー
	 */
	@Override
	public void action(float tpf, AWTEvent v) throws Exception
	{
		if( !(v instanceof KeyEvent))
		{
			return;
		}

		KeyEvent e = (KeyEvent) v;

		boolean isPressed = (e.getID() == KeyEvent.KEY_PRESSED);
		this.setFlag(e.getKeyCode(), isPressed, false);

	}

}
