package canvas2.util.flag;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * フラグの種類が決まっている{@link BasicFlags}
 */
public class FixedFlags<I> extends BasicFlags<I>{

	private Set<I> kind;

	public FixedFlags(Iterable<I> kind)
	{
		this.kind = this.createKindSet();
		for(I id : kind)
		{
			this.kind.add(id);
		}
	}

	public <T> Set<T> createKindSet()
	{
		return Collections.newSetFromMap(new IdentityHashMap<>());
	}

	@Override
	public void setFlag(I id, boolean flag, boolean canThrow)
	{
		if(this.kind.contains(id))
		{
			super.setFlag(id, flag, canThrow);
			return;
		}

		if(canThrow)
		{
			this.throwNotFound(id);
		}

	}

	protected void throwNotFound(I id)
	{
		throw new RuntimeException("not found id: " + id);
	}

	public Iterable<I> getKind()
	{
		return this.kind;
	}

	@Override
	public boolean isTrue(I id)
	{
		if(!this.kind.contains(id))
		{
			this.throwNotFound(id);
			return false;
		}

		return super.isTrue(id);
	}

	@Override
	public boolean isFalse(I id)
	{
		if(!this.kind.contains(id))
		{
			this.throwNotFound(id);
			return false;
		}

		return super.isFalse(id);
	}

	@Override
	public int getAllCount()
	{
		return this.kind.size();
	}

}
