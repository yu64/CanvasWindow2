package canvas2.value.file.test;

public class TestObject2 {

	private String text = "";
	
	public TestObject2(TestObject1... obj)
	{
		String enter = System.lineSeparator();
		
		int index = 0;
		for(TestObject1 t : obj)
		{
			this.text += "[" + index + "]" + t + enter;
			this.text += t.getText() + enter;
			
			index++;
		}
	}
	
	public String getText()
	{
		return this.text;
	}
}
