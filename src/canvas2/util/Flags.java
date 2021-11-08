package canvas2.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * フラグを管理するクラス。
 *
 */
public class Flags<I> {


	private Map<I, Boolean> flag = new HashMap<>();
	private int trueCount = 0;

	/**
	 * 識別子の種類を指定して作成。
	 */
	@SafeVarargs
	public Flags(I... id)
	{
		Objects.requireNonNull(id);

		for(I s : id)
		{
			Objects.requireNonNull(s);
			this.flag.put(s, false);
		}
	}

	/**
	 * 識別子の種類を指定して作成。
	 */
	public Flags(Set<I> id)
	{
		Objects.requireNonNull(id);

		for(I s : id)
		{
			Objects.requireNonNull(s);
			this.flag.put(s, false);
		}
	}

	/**
	 * キーの状態を示すフラグを変更する。
	 */
	public void setFlag(I id, boolean flag, boolean canThrow)
	{
		Boolean b = this.flag.get(id);

		//存在するキーで、
		//現在の値が設定される値と異なるとき、値を変更する。
		if(b != null && b.equals(flag))
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

	/**
	 * フラグを有効状態にする。
	 */
	public void enable(I id, boolean canThrow)
	{
		this.setFlag(id, true, canThrow);
	}

	/**
	 * フラグを無効状態にする。
	 */
	public void disable(I id, boolean canThrow)
	{
		this.setFlag(id, false, canThrow);
	}

	/**
	 * 指定した識別子のフラグが有効状態であるか
	 */
	public boolean isTrue(I id)
	{
		Boolean b = this.flag.get(id);
		if(b == null)
		{
			this.throwNotFindKey(id);
		}

		return b;
	}


	private void throwNotFindKey(I id)
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





}
