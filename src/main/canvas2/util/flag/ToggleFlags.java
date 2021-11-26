package canvas2.util.flag;

import canvas2.util.flag.Flags.ChangeListener;

public class ToggleFlags<I> extends BasicFlags<I> implements ChangeListener<I>{

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
		}

		if(!fromOffToOn && prev && !next)
		{
			this.flipFlag(id, false);
		}
	}







}
