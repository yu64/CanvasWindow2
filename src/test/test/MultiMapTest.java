package test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import canvas2.core.debug.TextTree;
import canvas2.util.MultiKeyMap;

class MultiMapTest {

	@BeforeEach
	public void setup()
	{
		System.out.println(this.getClass().getSimpleName());
	}

	@Test
	void test1()
	{
		MultiKeyMap<String, Integer> map = new MultiKeyMap<>(2);

		map.put(1, "A1", "A2");
		map.put(2, "A1", "B2");
		map.put(3, "B1", "A2");
		map.put(4, "B1", "B2");

		System.out.println(map);
		System.out.println();

		Assertions.assertEquals(3, map.get("B1", "A2"));
	}

	@Test
	void test2()
	{
		MultiKeyMap<String, Integer> map = new MultiKeyMap<>(2);

		map.put(1, "A1", "A2");
		map.put(2, "A1", "B2");
		map.put(3, "B1", "A2");
		map.put(4, "B1", "B2");

		System.out.println(map);

		Set<Integer> set2 = Set.of(1, 2, 3, 4);


		Set<Integer> set1 = new HashSet<>();
		map.forEach((k, v) -> {

			System.out.println(k + " -> " + v);
			set1.add(v);
		});

		System.out.println(set1 + " == " + set2);
		System.out.println();

		Assertions.assertTrue(set1.containsAll(set2));
	}

	@Test
	void test3()
	{
		MultiKeyMap<String, Integer> map = new MultiKeyMap<>(3);

		map.put(1, "A1", "A2", "A3");
		map.put(2, "A1", "B2", "A3");
		map.put(3, "A1", "B2", "B3");
		map.put(4, "B1", "A2", "A3");
		map.put(5, "B1", "B2", "A3");
		map.put(6, "B1", "B2", "B3");

		System.out.println(map);
		System.out.println(TextTree.getText(map));


		Set<Integer> set2 = Set.of(1, 2, 3);

		Set<Integer> set1 = new HashSet<>();
		String[] key1 = {"A1"};
		map.forEach(key1, (k, v) -> {

			System.out.println(k + " -> " + v);
			set1.add(v);
		});

		System.out.println(Arrays.toString(key1) + " " + set1 + " == " + set2);
		Assertions.assertTrue(set1.containsAll(set2));

		System.out.println();
	}

	@Test
	void test4()
	{
		MultiKeyMap<String, Integer> map = new MultiKeyMap<>(3);

		map.put(1, "A1", "A2", "A3");
		map.put(2, "A1", "B2", "A3");
		map.put(3, "A1", "B2", "B3");
		map.put(4, "B1", "A2", "A3");
		map.put(5, "B1", "B2", "A3");
		map.put(6, "B1", "B2", "B3");

		System.out.println(map);
		System.out.println(TextTree.getText(map));

		Set<Integer> set2 = Set.of(5, 6);


		Set<Integer> set1 = new HashSet<>();
		String[] key1 = {"B1", "B2"};
		map.forEach(key1, (k, v) -> {

			set1.add(v);
		});

		System.out.println(Arrays.toString(key1) + " " + set1 + " == " + set2);
		Assertions.assertTrue(set1.containsAll(set2));

		System.out.println();

	}

	@Test
	void test5()
	{
		MultiKeyMap<String, Integer> map = new MultiKeyMap<>(3);

		map.put(1, "A1", "A2", "A3");
		map.put(2, "A1", "B2", "A3");
		map.put(3, "A1", "B2", "B3");
		map.put(4, "B1", "A2", "A3");
		map.put(5, "B1", "B2", "A3");
		map.put(6, "B1", "B2", "B3");

		System.out.println(map);
		System.out.println(TextTree.getText(map));

		map.remove("A1", "A2", "A3");
		map.remove("A1", "B2", "A3");

		System.out.println("delete A1 A2 A3");
		System.out.println("delete A1 B2 A3");

		System.out.println(map);
		System.out.println(TextTree.getText(map));

		Set<Integer> set2 = Set.of(3);


		Set<Integer> set1 = new HashSet<>();
		String[] key1 = {"A1"};
		map.forEach(key1, (k, v) -> {

			set1.add(v);
		});

		System.out.println(Arrays.toString(key1) + " " + set1 + " == " + set2);
		Assertions.assertTrue(set1.containsAll(set2));

		System.out.println();

	}


	@Test
	void test6()
	{
		MultiKeyMap<String, Integer> map = new MultiKeyMap<>(3);

		map.put(1, "A1", "A2", "A3");
		map.put(2, "A1", "B2", "A3");
		map.put(3, "A1", "B2", "B3");
		map.put(4, "B1", "A2", "A3");
		map.put(5, "B1", "B2", "A3");
		map.put(6, "B1", "B2", "B3");

		System.out.println(map);
		System.out.println(TextTree.getText(map));


		String[] key1 = {"C1", "B2", "C3"};
		String[] key2 = {"A1", "C2", "B3"};
		String[] key3 = {"A1", "B2", "C3"};

		Assertions.assertEquals(null, map.get(key1));
		Assertions.assertEquals(null, map.get(key2));
		Assertions.assertEquals(null, map.get(key3));

		System.out.println();

	}


}
