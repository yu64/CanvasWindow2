package canvas2.util.flag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * フラグを管理するクラス。
 *
 */
public class BasicFlags<I> implements Flags<I>{


	private Map<I, Boolean> flag;
	private int trueCount = 0;

	/**
	 * 識別子の種類を指定して作成。
	 */
	@SafeVarargs
	public BasicFlags(I... id)
	{
		Objects.requireNonNull(id);

		this.flag = this.createMap();
		for(I s : id)
		{
			Objects.requireNonNull(s);
			this.flag.put(s, false);
		}
	}

	/**
	 * 識別子の種類を指定して作成。
	 */
	public BasicFlags(Set<I> id)
	{
		Objects.requireNonNull(id);

		this.flag = this.createMap();
		for(I s : id)
		{
			Objects.requireNonNull(s);
			this.flag.put(s, false);
		}
	}

	protected Map<I, Boolean> createMap()
	{
		return new HashMap<>();
	}




	public void setFlag(I id, boolean flag, boolean canThrow)
	{
		Boolean b = this.flag.get(id);

		//存在するキーで、
		//現在の値が設定される値と異なるとき、値を変更する。
		if(b != null && !b.equals(flag))
		{
			this.flag.put(id, flag);
			this.trueCount += (flag ? 1 : -1);
			return;
		}

		if(b == null && canThrow)
		{
			this.throwNotFindKey(id);
		}

	}



	public void enable(I id, boolean canThrow)
	{
		this.setFlag(id, true, canThrow);
	}

	public void disable(I id, boolean canThrow)
	{
		this.setFlag(id, false, canThrow);
	}




	public boolean isTrue(I id)
	{
		Boolean b = this.flag.get(id);
		if(b == null)
		{
			this.throwNotFindKey(id);
		}

		return b;
	}

	@Override
	public boolean isFlase(I id)
	{
		return !this.isTrue(id);
	}

	protected void throwNotFindKey(I id)
	{
		throw new RuntimeException("not find : " + id);
	}


	public int getAllCount()
	{
		return this.flag.size();
	}

	public int getTrueCount()
	{
		return this.trueCount;
	}

	public int getFalseCount()
	{
		return this.flag.size() - this.trueCount;
	}


	public boolean isAllTrue()
	{
		return this.getTrueCount() == this.getAllCount();
	}

	public boolean isAllFalse()
	{
		return this.getFalseCount() == this.getAllCount();
	}

	@Override
	public Iterator<I> iterator()
	{
		return this.flag.keySet().iterator();
	}





}
