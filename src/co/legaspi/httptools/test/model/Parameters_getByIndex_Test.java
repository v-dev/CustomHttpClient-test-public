package co.legaspi.httptools.test.model;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import co.legaspi.httptools.model.KeyValuePair;
import co.legaspi.httptools.model.Pair;
import co.legaspi.httptools.model.Parameters;

public class Parameters_getByIndex_Test {
	private final static Parameters params = new Parameters();
	private final static Pair pair1 = new KeyValuePair("breakfast", "blueberry smoothie");
	private final static Pair pair2 = new KeyValuePair("hungry", "");
	private final static Pair pair3 = new KeyValuePair("Dinner", "beef Stew");
	
	@Rule
	public TestName name = new TestName();
	
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		params.add(pair1);
		params.add(pair2);
		params.add(pair3);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void pair1() {
		printTestName();
		
		System.out.println("pair1: " + pair1);
		Pair actual = params.getByIndex(0);
		System.out.println("actual: " + actual);
		assertEquals(pair1, actual);
	}
	
	@Test
	public void pair2() {
		printTestName();
		
		System.out.println("pair2: " + pair2);
		Pair actual = params.getByIndex(1);
		System.out.println("actual: " + actual);
		assertEquals(pair2, actual);
	}
	
	@Test
	public void pair3() {
		printTestName();
		
		System.out.println("pair3: " + pair3);
		Pair actual = params.getByIndex(2);
		System.out.println("actual: " + actual);
		assertEquals(pair3, actual);
	}
	
	@Test
	public void pair1_NotFound_DiffCaps_Value() {
		printTestName();
		
		Pair aDifferentPair = new KeyValuePair("breakfast", "blueberry Smoothie");   // capital S for Smoothie 
		System.out.println("aDifferentPair: " + aDifferentPair);
		Pair actual = params.getByIndex(0);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void pair2_NotFound_DiffCaps_Key() {
		printTestName();
		
		Pair aDifferentPair = new KeyValuePair("lUnch", "pasta");  // capital U in lUnch
		System.out.println("aDifferentPair: " + aDifferentPair);
		Pair actual = params.getByIndex(1);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void pair3_NotFound_DiffCaps_KeyAndValue() {
		printTestName();
		
		Pair aDifferentPair = new KeyValuePair("dinner", "Beef stew");  // lower d, S in dinner, stew; capital Beef
		System.out.println("aDifferentPair: " + aDifferentPair);
		Pair actual = params.getByIndex(2);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void getAllReturnsEmptyNotANull() {
		printTestName();

		Parameters actual = new Parameters();
		Parameters allheaders = actual.getAll(); 
		
		System.out.println(allheaders);
		
		assertThat(allheaders, CoreMatchers.notNullValue());
	}
	
	@Test
	public void getByIndexReturnsEmptyNotANull() {
		printTestName();
		
		Pair actual = params.getByIndex(5);
		System.out.println(actual);
		
		assertThat(actual, CoreMatchers.notNullValue());
	}

}
