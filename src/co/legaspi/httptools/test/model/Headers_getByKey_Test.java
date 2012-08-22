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

public class Headers_getByKey_Test {
	private final static Headers headers = new Headers();
	private final static Header header1 = new Header("breakfast", "blueberry smoothie");
	private final static Header header2 = new Header("lunch", "pasta");
	private final static Header header3 = new Header("Dinner", "beef Stew");
	
	@Rule
	public TestName name = new TestName();
	
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		headers.add(header1);
		headers.add(header2);
		headers.add(header3);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void onlyHeader1Returned() {
		printTestName();
		
		System.out.println("header1: " + header1);
		Headers actual = headers.getByKey(header1.getName());
		Header actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual.size(), CoreMatchers.is(1));
		assertThat(actual0, CoreMatchers.is(header1));
	}
	
	@Test
	public void header1ContentsMatch() {
		printTestName();
		
		System.out.println("header1: " + header1);
		Headers actual = headers.getByKey(header1.getName());
		Header actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual0.getName(), CoreMatchers.is(header1.getName()));
		assertThat(actual0.getValue(), CoreMatchers.is(header1.getValue()));
	}
	
	@Test
	public void onlyHeader2Returned() {
		printTestName();
		
		System.out.println("header2: " + header2);
		Headers actual = headers.getByKey(header2.getName());
		Header actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual.size(), CoreMatchers.is(1));
		assertThat(actual0, CoreMatchers.is(header2));
	}
	
	@Test
	public void header2ContentsMatch() {
		printTestName();
		
		System.out.println("header2: " + header2);
		Headers actual = headers.getByKey(header2.getName());
		Header actual0 = actual.getByIndex(0);
		
		System.out.println("actual: " + actual);
		
		assertThat(actual0.getName(), CoreMatchers.is(header2.getName()));
		assertThat(actual0.getValue(), CoreMatchers.is(header2.getValue()));
	}
	
	
	@Test
	public void header2_NotFound_DiffCaps_Key() {
		printTestName();
		
		Header aDifferentPair = new Header("lUnch", "pasta");  // capital U in lUnch
		System.out.println("aDifferentPair: " + aDifferentPair);
		Header actual = headers.getByKey(aDifferentPair.getName()).getByIndex(0);
		System.out.println("actual: " + actual);
		assertNotSame(aDifferentPair, actual);
	}
	
	@Test
	public void returnsMultiplePairs() {
		printTestName();
		String key = "food";
		
		Headers actual = new Headers();
		Header head1 = new Header(key, "pho");
		Header head2 = new Header(key, "English Muffin");
		Header head3 = new Header(key, "coffee");
		Header head4 = new Header(key, "biscuit");
		
		actual.add(head1);
		actual.add(head2);
		actual.add(head3);
		actual.add(head4);
		
		System.out.println("actual: " + actual);
		for ( Header header : actual ) {
			System.out.println(header);
		}
		
		assertThat(actual.getByKey(key).size(), CoreMatchers.is(4));
		assertThat(actual.getByKey(key).getByIndex(0), CoreMatchers.is(head1));
		assertThat(actual.getByKey(key).getByIndex(1), CoreMatchers.is(head2));
		assertThat(actual.getByKey(key).getByIndex(2), CoreMatchers.is(head3));
		assertThat(actual.getByKey(key).getByIndex(3), CoreMatchers.is(head4));
		
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
	public void getByKeyReturnsEmptyNotANull() {
		printTestName();
		
		Headers actual = headers.getByKey("does_not_exist");
		System.out.println(actual);
		
		assertThat(actual, CoreMatchers.notNullValue());
	}
	
	@Test
	public void getByNonExistentKeyReturnsZeroSize() {
		printTestName();
		
		Headers actual = headers.getByKey("non_existent");
		System.out.println("size: " + actual.size());
		
		assertThat(actual.size(), CoreMatchers.is(0));
	}

}
