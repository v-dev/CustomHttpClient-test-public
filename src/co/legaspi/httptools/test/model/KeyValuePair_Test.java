package co.legaspi.httptools.test.model;

import org.hamcrest.CoreMatchers;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import co.legaspi.httptools.model.KeyValuePair;

/**
 * test: every Key must have a non-null Value.
 */
public class KeyValuePair_Test {
	private static final String KEY = "color";
	private static final String VALUE = "blue";
	
	private KeyValuePair kvp = new KeyValuePair(KEY, VALUE);
	
	@Rule
	public TestName name = new TestName(); 
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}
	
	@Test
	public void cannotSetNameToEmpty() {
		printTestName();
		
		kvp.setName("");
		
		System.out.println(kvp);
		System.out.println("kvp.getName():" + kvp.getName());
		
		assertThat(kvp.getName(), CoreMatchers.not(""));
	}
	
	@Test
	public void cannotSetNameToNull() {
		printTestName();
		
		kvp.setName(null);
		
		System.out.println(kvp);
		System.out.println("kvp.getName():" + kvp.getName());
		
		assertThat(kvp.getName(), CoreMatchers.notNullValue());
	}
	
	@Test
	public void constructorDoesNotApplyNullName_UsesDefault() {
		printTestName();
		
		KeyValuePair attempt = new KeyValuePair(null, "blue");
		
		System.out.println(attempt);
		System.out.println("attempt.getName(): " + attempt.getName());
		
		assertThat(attempt.getName(), CoreMatchers.notNullValue());
		assertThat(attempt.getName(), CoreMatchers.is("key"));
	}
	
	@Test
	public void constructorDoesNotApplyEmptyName_UsesDefault() {
		printTestName();
		
		KeyValuePair attempt = new KeyValuePair("", "blue");
		
		System.out.println(attempt);
		System.out.println("attempt.getName(): " + attempt.getName());
		
		assertThat(attempt.getName(), CoreMatchers.not(""));
		assertThat(attempt.getName(), CoreMatchers.is("key"));
	}


	@Test
	public void cannotSetValueToNull() {
		printTestName();
		
		kvp.setValue(null);
		
		System.out.println(kvp);
		System.out.println("kvp.getValue():" + kvp.getValue());
		
		assertThat(kvp.getValue(), CoreMatchers.notNullValue());
	}
	
	@Test
	public void constructorDoesNotApplyNullValue() {
		printTestName();
		
		KeyValuePair attempt = new KeyValuePair("color", null);
		
		System.out.println(attempt);
		System.out.println("attempt.getValue(): " + attempt.getValue());
		
		assertThat(attempt.getValue(), CoreMatchers.notNullValue());
	}
	
	@Test
	public void valueCanBeSetEmpty() {
		printTestName();
		
		System.out.println("pre: " + kvp);
		
		kvp.setValue("");
		System.out.println("post: " + kvp);
		
		assertThat(kvp.getValue(), CoreMatchers.is(""));
	}
	
	@Test
	public void valueCanBeInstantiatedEmpty() {
		printTestName();
		
		KeyValuePair attempt = new KeyValuePair(KEY, "");
		System.out.println(attempt);
		
		assertThat(attempt.getValue(), CoreMatchers.is(""));
	}

}
