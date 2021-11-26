package canvas2.event.flag;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import canvas2.event.EventManager;
import canvas2.event.Registerable;
import canvas2.event.awt.AwtListener;
import canvas2.util.flag.FixedFlags;

/**
 * マウスのボタンを監視するクラス。
 *
 */
public class ButtonFlags extends FixedFlags<Integer>
	implements Registerable, AwtListener, MouseListener {



	/**
	 * マウスのボタンの種類を指定して作成。
	 */
	public ButtonFlags(Iterable<Integer> keys)
	{
		super(keys);
	}

	/**
	 * 指定されたボタンが押されたか
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
		event.add(AWTEvent.class, MouseEvent.MOUSE_PRESSED, this);
		event.add(AWTEvent.class, MouseEvent.MOUSE_RELEASED, this);
		event.add(AWTEvent.class, FocusEvent.FOCUS_LOST, this);
	}

	/**
	 * リスナー削除。
	 */
	@Override
	public void unregisterTo(EventManager event)
	{
		event.remove(AWTEvent.class, MouseEvent.MOUSE_PRESSED, this);
		event.remove(AWTEvent.class, MouseEvent.MOUSE_RELEASED, this);
		event.remove(AWTEvent.class, FocusEvent.FOCUS_LOST, this);
	}

	/**
	 * {@link MouseEvent}からキーのフラグを更新します。
	 */
	public void setPressedFlag(AWTEvent e)
	{
		if( !(e instanceof MouseEvent))
		{
			return;
		}

		this.setPressedFlag(((MouseEvent) e));
	}

	/**
	 * {@link MouseEvent}からキーのフラグを更新します。
	 */
	public void setPressedFlag(MouseEvent e)
	{
		boolean isPressed = (e.getID() == MouseEvent.MOUSE_PRESSED);
		this.setFlag(e.getButton(), isPressed, false);
	}

	/**
	 * キー入力を受け取るためのリスナー
	 */
	@Override
	public void act(float tpf, AWTEvent v) throws Exception
	{
		if(v instanceof FocusEvent)
		{
			for(Integer key : this.getKind())
			{
				this.setFlag(key, false, false);
			}
			return;
		}

		this.setPressedFlag(v);
	}




	/**
	 * リスナー登録。
	 */
	public void registerTo(Component c)
	{
		c.addMouseListener(this);
	}

	/**
	 * リスナー削除。
	 */
	public void unregisterTo(Component c)
	{
		c.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.setPressedFlag(e);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.setPressedFlag(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		this.setPressedFlag(e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}


}
