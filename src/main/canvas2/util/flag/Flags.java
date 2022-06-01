package canvas2.util.flag;

/**
 * フラグを管理するクラスのインターフェイス
 */
public interface Flags<I> {


	/**
	 * 指定した識別子に対応するフラグを変更する。
	 */
	public void setFlag(I id, boolean flag, boolean canThrow);

	/**
	 * 指定した識別子に対応するフラグを反転する。
	 */
	public void flipFlag(I id, boolean canThrow);

	/**
	 * フラグを有効状態にする。
	 */
	public void enable(I id, boolean canThrow);

	/**
	 * フラグを無効状態にする。
	 */
	public void disable(I id, boolean canThrow);


	/**
	 * 指定した識別子に対応するフラグが存在するか
	 */
	public boolean exist(I id);

	/**
	 * 指定した識別子に対応するフラグが有効状態であるか
	 */
	public boolean isTrue(I id);

	/**
	 * 指定した識別子に対応するフラグが無効状態であるか
	 */
	public boolean isFalse(I id);





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
	 * 指定した識別子に対応するフラグがすべて有効状態であるか
	 */
	@SuppressWarnings("unchecked")
	public boolean isAllTrue(I... id);

	/**
	 * 指定した識別子に対応するフラグがすべて有効状態であるか
	 */
	public boolean isAllTrueFromArray(I[] id);

	/**
	 * すべてfalseであるか
	 */
	public boolean isAllFalse();

	/**
	 * 指定した識別子に対応するフラグがすべて無効状態であるか
	 */
	@SuppressWarnings("unchecked")
	public boolean isAllFalse(I... id);

	/**
	 * 指定した識別子に対応するフラグがすべて無効状態であるか
	 */
	public boolean isAllFalseFromArray(I[] id);



	/**
	 * フラグ変更時に呼び出されるリスナーを設定する。<br>
	 * nullであるとき、呼び出されない。
	 */
	public void setChangeListener(ChangeListener<I> listener);


	/**
	 * フラグが変更されたときに呼び出されるリスナー<br>
	 * メソッド<br>
	 * {@link ChangeListener#onChange}
	 */
	@FunctionalInterface
	public static interface ChangeListener<I> {

		public void onChange(Flags<I> src, I id, boolean prev, boolean next, boolean isBefore);
	}

}
