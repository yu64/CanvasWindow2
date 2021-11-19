package canvas2.value.file.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import canvas2.file.LoaderManager;
import canvas2.file.ReadTicket;
import canvas2.file.Reader;

public class TestReader1 implements Reader<TestObject1>{

	@Override
	public Class<TestObject1> getDataClass()
	{
		return TestObject1.class;
	}

	@Override
	public void registerDependentTo(LoaderManager loader)
	{
		
	}

	@Override
	public TestObject1 read(LoaderManager loader, String path, Object op) throws Exception
	{
		InputStream is = TestReader1.class.getClassLoader().getResourceAsStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		
		String line = null;
		String output = "";
		String enter = System.lineSeparator();
		
		while((line = br.readLine()) != null)
		{
			output += line + enter;
		}
		
		br.close();
		
		
		return new TestObject1(output);
	}

	@Override
	public Set<ReadTicket<?>> getDependent(String path, Object op, int priority)
	{
		return null;
	}


}
