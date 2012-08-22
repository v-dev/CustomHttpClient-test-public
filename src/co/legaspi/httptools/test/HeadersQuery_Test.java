package co.legaspi.httptools.test;

import org.hamcrest.Matchers;

import static org.junit.Assert.*;

import org.junit.Test;

import co.legaspi.httptools.CustomHttpRequest;
import co.legaspi.httptools.CustomHttpRequestImpl;
import co.legaspi.httptools.CustomHttpResponse;
import co.legaspi.httptools.CustomHttpResponseImpl;
import co.legaspi.httptools.model.Header;
import co.legaspi.httptools.model.Headers;

public class HeadersQuery_Test {
	private static String HOST = "legaspi.co";
	private static String PATH = "/php/headers_query.php";
	
	private CustomHttpRequest request;
	private CustomHttpResponse response;


	@Test
	public void httpExtraHeaders_Get() {
		Headers headers = new Headers();
		Header head1 = new Header("thisIsTheName", "this is the value1");
		Header head2 = new Header("aDifferentHeader", "another value: 2");
		headers.add(head1);
		headers.add(head2);
		
		request = new CustomHttpRequestImpl.Builder(HOST, PATH).
				headers(headers).
				build();
		
		response = request.submit();
		String rawRequest = ((CustomHttpRequestImpl)request).getRawRequest();
		String rawResponse = ((CustomHttpResponseImpl)response).getRaw();
		
		System.out.println("Request: " + rawRequest);
		System.out.println("\n\nResponse: " + rawResponse);
		
		// Basic Asserts
		assertThat(rawResponse, Matchers.containsString("HTTP/1.1 200 OK"));
		assertThat(rawResponse, Matchers.containsString("HTTP_HOST = legaspi.co"));
		
		System.out.println("\nheaders: " + headers.toString());
		assertThat(rawRequest, Matchers.containsString(head1.getName() + ": " + head1.getValue()));
		assertThat(rawRequest, Matchers.containsString(head2.getName() + ": " + head2.getValue()));
		
		assertThat(rawResponse, Matchers.containsString("HTTP_" + head1.getName().toUpperCase() + " = " + head1.getValue()));
		assertThat(rawResponse, Matchers.containsString("HTTP_" + head2.getName().toUpperCase() + " = " + head2.getValue()));
	}
	
	
	//@Test
	public void httpExtraQueryParams_Get() {
		request = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		
		response = request.submit();
		String rawResponse = ((CustomHttpResponseImpl)response).getRaw();
		
		System.out.println("Request: " + ((CustomHttpRequestImpl)request).getRawRequest());
		System.out.println("\n\nResponse: " + rawResponse);
		
		// Basic asserts
		assertThat(rawResponse, Matchers.containsString("HTTP/1.1 200 OK"));
		assertThat(rawResponse, Matchers.containsString("HTTP_HOST = legaspi.co"));
	}

}