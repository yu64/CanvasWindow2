package canvas2.state;

/**
 * 状態を示す、値を一つ持つクラス。<br>
 * 不変<br>
 *
 */
public class StateObj<T> implements State {

	private T value;

	public StateObj(T value)
	{
		this.value = value;
	}

	public T get()
	{
		return this.value;
	}
}
