package canvas2.value.file.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import canvas2.file.LoaderManager;
import canvas2.file.ReadTicket;
import canvas2.file.Reader;

public class TestReader2 implements Reader<TestObject2>{

	@Override
	public Class<TestObject2> getDataClass()
	{
		return TestObject2.class;
	}

	@Override
	public void registerDependentTo(LoaderManager loader)
	{
		if(!loader.hasReader(TestObject1.class))
		{
			loader.register(new TestReader1());
		}
	}

	@Override
	public TestObject2 read(LoaderManager loader, String path, Object op) throws Exception
	{
		InputStream is = TestReader2.class.getClassLoader().getResourceAsStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		List<TestObject1> list = br.lines()
			.filter(s -> !s.isBlank())
			.map(s -> loader.get(TestObject1.class, s).getResult())
			.collect(Collectors.toList());
		
		br.close();
		
		
		
		
		return new TestObject2(list.toArray(new TestObject1[0]));
	}

	@Override
	public Set<ReadTicket<?>> getDependent(String path, Object op, int priority) throws Exception
	{
		InputStream is = TestReader2.class.getClassLoader().getResourceAsStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		
		Set<ReadTicket<?>> set = br.lines()
			.filter(s -> !s.isBlank())
			.peek(s -> System.out.println(s))
			.map(s -> new ReadTicket<>(TestObject1.class, s))
			.collect(Collectors.toSet());
		
		br.close();
		
		return set;
	}


}
