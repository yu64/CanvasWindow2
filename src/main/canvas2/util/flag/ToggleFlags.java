package canvas2.util.flag;

import canvas2.util.flag.Flags.ChangeListener;

/**
 * 別の{@link Flags}に変化があるたびにフラグが反転する{@link BasicFlags}
 */
public class ToggleFlags<I> extends BasicFlags<I> implements ChangeListener<I>{

	/**
	 * オフからオンに切り替わったことを検出したとき、
	 * フラグを切り替えるか
	 */
	private boolean fromOffToOn;

	public ToggleFlags(boolean fromOffToOn)
	{
		this.fromOffToOn = fromOffToOn;
	}

	@Override
	public void onChange(Flags<I> src, I id, boolean prev, boolean next, boolean isBefore)
	{
		boolean fromOffToOn = this.fromOffToOn;

		if(fromOffToOn && !prev && next)
		{
			this.flipFlag(id, false);
			return;
		}

		if(!fromOffToOn && prev && !next)
		{
			this.flipFlag(id, false);;
			return;
		}
	}







}
