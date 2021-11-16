package canvas2.state;

/**
 * 状態を示す、文字列を一つ持つクラス。<br>
 * 不変<br>
 */
public class StateName implements State{

	private String name;

	public StateName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public String toString()
	{
		return this.getClass().getSimpleName() + ": " + this.name;
	}
}
