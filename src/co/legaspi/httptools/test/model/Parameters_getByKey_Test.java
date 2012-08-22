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

public class Parameters_getByKey_Test {
	private final static Parameters parameters = new Parameters();
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
		parameters.add(pair1);
		parameters.add(pair2);
		parameters.add(pair3);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void onlyHeader1Returned() {
		printTestName();
		
		System.out.println("pair1: " + pair1);
		Parameters actual = parameters.getByKey(pair1.getName());
		Pair actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual.size(), CoreMatchers.is(1));
		assertThat(actual0, CoreMatchers.is(pair1));
	}
	
	@Test
	public void header1ContentsMatch() {
		printTestName();
		
		System.out.println("pair1: " + pair1);
		Parameters actual = parameters.getByKey(pair1.getName());
		Pair actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual0.getName(), CoreMatchers.is(pair1.getName()));
		assertThat(actual0.getValue(), CoreMatchers.is(pair1.getValue()));
	}
	
	@Test
	public void onlyHeader2Returned() {
		printTestName();
		
		System.out.println("pair2: " + pair2);
		Parameters actual = parameters.getByKey(pair2.getName());
		Pair actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual.size(), CoreMatchers.is(1));
		assertThat(actual0, CoreMatchers.is(pair2));
	}
	
	@Test
	public void header2ContentsMatch() {
		printTestName();
		
		System.out.println("pair2: " + pair2);
		Parameters actual = parameters.getByKey(pair2.getName());
		Pair actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual0.getName(), CoreMatchers.is(pair2.getName()));
		assertThat(actual0.getValue(), CoreMatchers.is(pair2.getValue()));
	}
	
	
	@Test
	public void header2_NotFound_DiffCaps_Key() {
		printTestName();
		
		Pair aDifferentPair = new KeyValuePair("lUnch", "pasta");  // capital U in lUnch
		System.out.println("aDifferentPair: " + aDifferentPair);
		Pair actual = parameters.getByKey(aDifferentPair.getName()).getByIndex(0);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void returnsMultiplePairs() {
		printTestName();
		String key = "food";
		
		Parameters actual = new Parameters();
		Pair p1 = new KeyValuePair(key, "pho");
		Pair p2 = new KeyValuePair(key, "English Muffin");
		Pair p3 = new KeyValuePair(key, "coffee");
		Pair p4 = new KeyValuePair(key, "biscuit");
		
		actual.add(p1);
		actual.add(p2);
		actual.add(p3);
		actual.add(p4);
		
		System.out.println("actual: " + actual);
		for ( Pair pair : actual ) {
			System.out.println(pair);
		}
		
		assertThat(actual.getByKey(key).size(), CoreMatchers.is(4));
		assertThat(actual.getByKey(key).getByIndex(0), CoreMatchers.is(p1));
		assertThat(actual.getByKey(key).getByIndex(1), CoreMatchers.is(p2));
		assertThat(actual.getByKey(key).getByIndex(2), CoreMatchers.is(p3));
		assertThat(actual.getByKey(key).getByIndex(3), CoreMatchers.is(p4));
		
	}
	
	@Test
	public void getAllReturnsEmptyNotANull() {
		printTestName();

		Parameters actual = new Parameters();
		Parameters allparams = actual.getAll(); 
		
		System.out.println(allparams);
		
		assertThat(allparams, CoreMatchers.notNullValue());
	}
	
	@Test
	public void getByKeyReturnsEmptyNotANull() {
		printTestName();
		
		Parameters actual = parameters.getByKey("does_not_exist");
		System.out.println(actual);
		
		assertThat(actual, CoreMatchers.notNullValue());
	}
	
	@Test
	public void getByNonExistentKeyReturnsZeroSize() {
		printTestName();
		
		Parameters actual = parameters.getByKey("non_existent");
		System.out.println("size: " + actual.size());
		
		assertThat(actual.size(), CoreMatchers.is(0));
	}

}
