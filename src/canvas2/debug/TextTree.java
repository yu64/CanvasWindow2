package canvas2.debug;

public interface TextTree {

	public static String getText(TextTree obj)
	{
		StringBuilder sb = new StringBuilder();
		sb = obj.createTreeText(sb, 0);
		return sb.toString();
	}
	
	public StringBuilder createTreeText(StringBuilder sb, int nest);
	
}
