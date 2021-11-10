package canvas2.debug;

public interface TextTree {

	/**
	 * 指定したオブジェクトの構造を文字列で取得する。<br>
	 * 人間が構造を確認する目的で使うことを推奨する。
	 */
	public static String getText(TextTree obj)
	{
		StringBuilder sb = new StringBuilder();
		sb = obj.createTreeText(sb, 0);
		return sb.toString();
	}

	/**
	 * 実装されたオブジェクトの内部構造を示す文字列を生成する。<br>
	 * 人間が構造を確認する目的で使うことを推奨する。<br>
	 * 簡潔に文字列を取得したい場合、
	 * {@link TextTree#getText(TextTree)}を用いることを推奨する。
	 *
	 */
	public StringBuilder createTreeText(StringBuilder sb, int nest);

}
