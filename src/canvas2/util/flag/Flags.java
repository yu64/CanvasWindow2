package canvas2.util.flag;

import java.util.Iterator;

public interface Flags<I> extends Iterable<I>{


	/**
	 * 指定した識別子に対応するフラグを変更する。
	 */
	public void setFlag(I id, boolean flag, boolean canThrow);

	/**
	 * フラグを有効状態にする。
	 */
	public void enable(I id, boolean canThrow);

	/**
	 * フラグを無効状態にする。
	 */
	public void disable(I id, boolean canThrow);



	/**
	 * 指定した識別子に対応するフラグが有効状態であるか
	 */
	public boolean isTrue(I id);

	/**
	 * 指定した識別子に対応するフラグが無効状態であるか
	 */
	public boolean isFlase(I id);



	/**
	 * すべてのフラグの数を取得
	 */
	public int getAllCount();

	/**
	 * trueの数を取得
	 */
	public int getTrueCount();

	/**
	 * falseの数を取得
	 */
	public int getFalseCount();

	/**
	 * すべてtrueであるか
	 */
	public boolean isAllTrue();

	/**
	 * すべてfalseであるか
	 */
	public boolean isAllFalse();


	/**
	 * すべての識別子を返す。
	 */
	@Override
	public Iterator<I> iterator();

}
