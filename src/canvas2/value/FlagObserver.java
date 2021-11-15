package canvas2.value;

import canvas2.core.Updatable;
import canvas2.util.flag.Flags;

public class FlagObserver<I> implements Updatable{


	private Flags<I> flags;

	public FlagObserver(Flags<I> flags)
	{
		this.flags = flags;
	}

	@Override
	public void update(float tpf) throws Exception
	{

	}
}
