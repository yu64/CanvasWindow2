package canvas2.util.flag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * フラグを管理するクラス。
 *
 */
public class BasicFlags<I> implements Flags<I>{


	private Set<I> flag;
	private ChangeListener<I> listener;

	public BasicFlags()
	{
		this.flag = this.createSet();
	}

	protected Map<I, Boolean> createMap()
	{
		return new HashMap<>();
	}

	protected Set<I> createSet()
	{
		return new HashSet<>();
	}

	@Override
	public void setFlag(I id, boolean flag, boolean canThrow)
	{
		boolean b = this.flag.contains(id);

		//現在の値が設定される値と異なるとき、値を変更する。
		if(b == flag)
		{
			return;
		}

		if(flag)
		{
			this.flag.add(id);
		}
		else
		{
			this.flag.remove(id);
		}


		if(this.listener != null)
		{
			this.listener.onChange(this, id, b, flag);
		}

	}

	@Override
	public void flipFlag(I id, boolean canThrow)
	{
		boolean b = this.isTrue(id);
		this.setFlag(id, !b, canThrow);
	}



	@Override
	public void enable(I id, boolean canThrow)
	{
		this.setFlag(id, true, canThrow);
	}

	@Override
	public void disable(I id, boolean canThrow)
	{
		this.setFlag(id, false, canThrow);
	}



	@Override
	public boolean exist(I id)
	{
		return true;
	}

	@Override
	public boolean isTrue(I id)
	{
		return this.flag.contains(id);
	}

	@Override
	public boolean isFlase(I id)
	{
		return !this.isTrue(id);
	}


	@Override
	public int getAllCount()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getTrueCount()
	{
		return this.flag.size();
	}

	@Override
	public int getFalseCount()
	{
		return this.getAllCount() - this.getTrueCount();
	}


	@Override
	public boolean isAllTrue()
	{
		return this.getTrueCount() == this.getAllCount();
	}

	@Override
	public boolean isAllFalse()
	{
		return this.getFalseCount() == this.getAllCount();
	}

	@Override
	public void setListener(ChangeListener<I> listener)
	{
		this.listener = listener;
	}








}
