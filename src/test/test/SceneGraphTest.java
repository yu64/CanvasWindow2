package test;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import canvas2.core.Drawable;
import canvas2.core.debug.TextTree;
import canvas2.view.scene.Node;

class SceneGraphTest {


	private Node root;
	private Node groupA;
	private Node groupB;
	private Node leafA1;
	private Node leafA2;
	private Node leafB1;

	private static class TestNode extends Node {

		public TestNode(String name)
		{
			super(name);
		}

		@Override
		protected Collection<Drawable> createCollection()
		{
			return new LinkedHashSet<>();
		}
	}



	/* JUnit5のときは、@Before ではなく、@BeforeEach を使用する */

	@BeforeEach
	public void setup()
	{
		this.root = new TestNode("root");
		this.groupA = new TestNode("groupA");
		this.groupB = new TestNode("groupB");
		this.leafA1 = new TestNode("leafA1");
		this.leafA2 = new TestNode("leafA2");
		this.leafB1 = new TestNode("leafB1");

	}

	@Test
	@Ignore
	public void test1()
	{
		System.out.println("[" + this.getClass().getSimpleName() + "] setup");

		String enter = System.lineSeparator();
		String answer = "";

		answer += this.root + enter;
		answer += "\t" + this.groupA + enter;
		answer += "\t\t" + this.leafA1 + enter;
		answer += "\t\t" + this.leafA2 + enter;
		answer += "\t" + this.groupB + enter;
		answer += "\t\t" + this.leafB1 + enter;

		System.out.println("answer");
		System.out.println(answer);


		this.root.add(this.groupA);
		this.groupA.add(this.leafA1);
		this.groupA.add(this.leafA2);

		this.root.add(this.groupB);
		this.groupB.add(this.leafB1);

		String output = TextTree.getText(this.root);

		System.out.println("output");
		System.out.println(output);

		Assertions.assertEquals(answer, output);
	}


}
