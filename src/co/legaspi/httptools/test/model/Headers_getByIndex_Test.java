package co.legaspi.httptools.test.model;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import co.legaspi.httptools.model.Header;
import co.legaspi.httptools.model.Headers;

public class Headers_getByIndex_Test {
	private final static Headers headers = new Headers();
	private final static Header pair1 = new Header("breakfast", "blueberry smoothie");
	private final static Header pair2 = new Header("lunch", "pasta");
	private final static Header pair3 = new Header("Dinner", "beef Stew");
	
	@Rule
	public TestName name = new TestName();
	
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		headers.add(pair1);
		headers.add(pair2);
		headers.add(pair3);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void pair1() {
		printTestName();
		
		System.out.println("pair1: " + pair1);
		Header actual = headers.getByIndex(0);
		System.out.println("actual: " + actual);
		assertEquals(pair1, actual);
	}
	
	@Test
	public void pair2() {
		printTestName();
		
		System.out.println("pair2: " + pair2);
		Header actual = headers.getByIndex(1);
		System.out.println("actual: " + actual);
		assertEquals(pair2, actual);
	}
	
	@Test
	public void pair3() {
		printTestName();
		
		System.out.println("pair3: " + pair3);
		Header actual = headers.getByIndex(2);
		System.out.println("actual: " + actual);
		assertEquals(pair3, actual);
	}
	
	@Test
	public void pair1_NotFound_DiffCaps_Value() {
		printTestName();
		
		Header aDifferentPair = new Header("breakfast", "blueberry Smoothie");   // capital S for Smoothie 
		System.out.println("aDifferentPair: " + aDifferentPair);
		Header actual = headers.getByIndex(0);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void pair2_NotFound_DiffCaps_Key() {
		printTestName();
		
		Header aDifferentPair = new Header("lUnch", "pasta");  // capital U in lUnch
		System.out.println("aDifferentPair: " + aDifferentPair);
		Header actual = headers.getByIndex(1);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void pair3_NotFound_DiffCaps_KeyAndValue() {
		printTestName();
		
		Header aDifferentPair = new Header("dinner", "Beef stew");  // lower d, S in dinner, stew; capital Beef
		System.out.println("aDifferentPair: " + aDifferentPair);
		Header actual = headers.getByIndex(2);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void getAllReturnsEmptyNotANull() {
		printTestName();

		Headers actual = new Headers();
		Headers allheaders = actual.getAll(); 
		
		System.out.println(allheaders);
		
		assertThat(allheaders, CoreMatchers.notNullValue());
	}
	
	@Test
	public void getByIndexReturnsEmptyNotANull() {
		printTestName();
		
		Header actual = headers.getByIndex(5);
		System.out.println(actual);
		
		assertThat(actual, CoreMatchers.notNullValue());
	}

}
