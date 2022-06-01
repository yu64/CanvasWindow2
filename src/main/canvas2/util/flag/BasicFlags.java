package canvas2.util.flag;

import java.util.HashSet;
import java.util.Set;

/**
 * フラグを管理するクラス。
 * フラグの種類は、自由に追加できる。
 *
 */
public class BasicFlags<I> implements Flags<I>{


	private Set<I> flag;
	private ChangeListener<I> listener;

	public BasicFlags()
	{
		this.flag = this.createSet();
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

		if(this.listener != null)
		{
			this.listener.onChange(this, id, b, flag, true);
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
			this.listener.onChange(this, id, b, flag, false);
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
	public boolean isFalse(I id)
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

	@SafeVarargs
	@Override
	public final boolean isAllTrue(I... id)
	{
		for(I i : id)
		{
			if(!this.isTrue(i))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public final boolean isAllTrueFromArray(I[] id)
	{
		return this.isAllTrue(id);
	}



	@Override
	public boolean isAllFalse()
	{
		return this.getFalseCount() == this.getAllCount();
	}

	@SafeVarargs
	@Override
	public final boolean isAllFalse(I... id)
	{
		for(I i : id)
		{
			if(!this.isFalse(i))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public final boolean isAllFalseFromArray(I[] id)
	{
		return this.isAllFalse(id);
	}




	@Override
	public void setChangeListener(ChangeListener<I> listener)
	{
		this.listener = listener;
	}









}
