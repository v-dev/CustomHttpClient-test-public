package co.legaspi.httptools.test;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import co.legaspi.httptools.CustomHttpRequest;
import co.legaspi.httptools.CustomHttpRequestImpl;
import co.legaspi.httptools.CustomHttpResponse;
import co.legaspi.httptools.CustomHttpResponseImpl;
import co.legaspi.httptools.model.CustomUrlEncodedFormEntity;
import co.legaspi.httptools.model.Headers;
import co.legaspi.httptools.model.ICustomHttpEntity;
import co.legaspi.httptools.model.KeyValuePair;
import co.legaspi.httptools.model.Method;
import co.legaspi.httptools.model.Pair;
import co.legaspi.httptools.model.Protocol;
import co.legaspi.httptools.model.ProxyInfo;

public class CustomRequestResponseTest {
	
	private static final String PROXY_HOST = "localhost";
	private static final int PROXY_PORT = 8888;
	
	private static final ProxyInfo proxy = new ProxyInfo(Protocol.HTTP, PROXY_HOST, PROXY_PORT);
	
	
	private static final String USER_AGENT = "a_java_tool"; 
	
	CustomHttpRequest req;
	CustomHttpResponse res;
	
	@Rule
	public TestName name = new TestName(); 
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}
	
	@BeforeClass
	public static void setupProxy() {
		//proxy.enableProxy();
		//proxy.disableProxy();
	}

	//@Ignore
	@Test
	public void simpleGet200Ok() {
		printTestName();
		
		req = new CustomHttpRequestImpl
				.Builder("legaspi.co", "/php/headers_test.php")
				.proxy(proxy)
				.addQueryParam("format", "cli")
				.addHeader("User-Agent", USER_AGENT)
				.build();
		res = req.submit();
		
		//System.out.println("\nraw: " + res.getRaw());
		//System.out.println("\n\nentity: " + res.getEntity());
		
		// Headers - getRaw()
		assertThat(res.getRaw(), CoreMatchers.containsString("HTTP/1.1 200 OK"));
		assertThat(res.getRaw(), CoreMatchers.containsString("X-Powered-By: PHP/5.2.17"));
		assertThat(res.getRaw(), CoreMatchers.containsString("Content-Type: text/html"));
		
		// Body - getEntity()
		//assertThat(res.getEntity(), CoreMatchers.containsString("HTTP_USER_AGENT = Apache-HttpClient/4.1.2 (java 1.5)"));
		assertThat(res.getEntity(), CoreMatchers.containsString("HTTP_USER_AGENT = " + USER_AGENT));
		assertThat(res.getEntity(), CoreMatchers.containsString("HTTP_HOST = legaspi.co"));
	}
	
	//@Ignore
	@Test
	public void simpleGet404() {
		printTestName();
		
		req = new CustomHttpRequestImpl
				.Builder("legaspi.co", "/php/headers_testX.php")
				.proxy(proxy)
				.addQueryParam("format", "cli")
				.build();
		res = req.submit();
		
		//System.out.println("\nraw: " + res.getRaw());
		//System.out.println("\n\nentity: " + res.getEntity());
		
		// Headers - getRaw()
		assertThat(res.getRaw(), CoreMatchers.containsString("HTTP/1.1 404 Not Found"));
		assertThat(res.getRaw(), CoreMatchers.containsString("Content-Type: text/html"));
		assertThat(res.getRaw(), CoreMatchers.containsString("X-Powered-By: PHP/5.2.17"));
		
		// Body - getEntity()
		assertThat(res.getRaw(), CoreMatchers.containsString("Sorry, the page you are looking for does not exist"));
		assertThat(res.getEntity(), CoreMatchers.containsString("<title>404 - Page Not Found</title>"));
	}
	
	//@Ignore
	@Test
	public void getWithCookiesAttached() {
		printTestName();
		
		req = new CustomHttpRequestImpl
				.Builder("legaspi.co", "/php/headers_test.php")
				.proxy(proxy)
				.addQueryParam("format", "cli")
				.addHeader("User-Agent", USER_AGENT)
				.addHeader("Cookie", "verna_cookie:follow me!")
				.build();
		
		res = req.submit();
		
		//System.out.println("\ngetWithCookiesAttached, res.getRaw():\n" + res.getRaw());
		//System.out.println("\ngetWithCookiesAttached, res.getHeaders(): " + ((CustomHttpResponseImpl)res).getHeaders());
		
		assertThat(res.getEntity(), CoreMatchers.containsString("HTTP_COOKIE = verna_cookie:follow me!"));
	}
	
	//@Ignore
	@Test
	public void getWithCookiesReturned() {
		printTestName();
		
		req = new CustomHttpRequestImpl
			.Builder("legaspi.co", "/php/cookie_test.php")
			//.Builder("localhost", "/test/cookie_test.php")
			.proxy(proxy)
			.addQueryParam("format", "cli")
			.addHeader("User-Agent", USER_AGENT)
			.addHeader("Connection", "close")
			.build();
		
		res = req.submit();
		Headers headers = ((CustomHttpResponseImpl)res).getHeaders();
		
		//System.out.println("\ngetWithCookiesReturned, res.getRaw():\n" + res.getRaw());
		//System.out.println("\ngetWithCookiesReturned, res.getHeaders(): " + headers);
		
		Headers setcookieHeaders = headers.getByKey("Set-Cookie");
		assertThat(setcookieHeaders.size(), CoreMatchers.is(1));
		assertThat(setcookieHeaders.getByIndex(0).getValue(), CoreMatchers.is("Verna-Cookie=chocolate+is+preferred"));
	}
	
	
	//@Ignore
	@Test
	public void getCookiesAndResendThem() {
		printTestName();
		
		// ---------------- part 1 ----------------//
		req = new CustomHttpRequestImpl
			.Builder("legaspi.co", "/php/cookie_test.php")
			//.Builder("localhost", "/test/cookie_test.php")
			.proxy(proxy)
			.addQueryParam("format", "cli")
			.addHeader("User-Agent", USER_AGENT)
			.addHeader("Connection", "close")
			.build();
		
		res = req.submit();
		Headers responseHeaders = ((CustomHttpResponseImpl)res).getHeaders();
		Headers responseSetCookies = ((CustomHttpResponseImpl)res).getSetCookies();
		
		//System.out.println("\ngetCookiesAndResendThem [1], res.getRaw():\n" + res.getRaw());
		//System.out.println("\ngetCookiesAndResendThem [1], res.getHeaders(): " + headers);
		//System.out.println("\ngetCookiesAndResendThem [1], responseSetCookies: " + responseSetCookies);
		
		// headers
		Headers setcookieHeaders = responseHeaders.getByKey("Set-Cookie");
		assertThat(setcookieHeaders.size(), CoreMatchers.is(1));
		assertThat(setcookieHeaders.getByIndex(0).getValue(), CoreMatchers.is("Verna-Cookie=chocolate+is+preferred"));
		
		assertThat(responseHeaders.getByKey("Cookie").size() , CoreMatchers.is(0));
		
		// cookies
		assertThat(responseSetCookies.getByIndex(0).getValue(), CoreMatchers.is("Verna-Cookie=chocolate+is+preferred"));
		assertThat(responseSetCookies.getByKey("Set-Cookie").size() , CoreMatchers.is(0));
		
		// old NVPs
		//assertTrue(responseSetCookies.contains(new BasicNameValuePair("Cookie", "Verna-Cookie=chocolate+is+preferred")));
		//assertFalse(responseSetCookies.contains(new BasicNameValuePair("Set-Cookie", "Verna-Cookie=chocolate+is+preferred")));
			//System.out.println("responseSetCookies.getByKey(Set-Cookie): " + responseSetCookies.getByKey("Set-Cookie"));
		
		// ---------------- part 2 ----------------//
		req = new CustomHttpRequestImpl
				.Builder("legaspi.co", "/php/headers_test.php")
				.proxy(proxy)
				.addQueryParam("format", "cli")
				.addHeader("User-Agent", USER_AGENT)
				.addHeader("Connection", "close")
				.headers(responseSetCookies)
				.build();
		
		res = req.submit();
		responseHeaders = ((CustomHttpResponseImpl)res).getHeaders();
		responseSetCookies = ((CustomHttpResponseImpl)res).getSetCookies();
		
		System.out.println("\ngetCookiesAndResendThem [2], res.getRaw():\n" + res.getRaw());
		System.out.println("\ngetCookiesAndResendThem [2], res.getHeaders(): " + responseHeaders);
		
		// headers  -- should no longer have Set-Cookie headers
		setcookieHeaders = responseHeaders.getByKey("Set-Cookie");
		assertThat(setcookieHeaders.size(), CoreMatchers.is(0));
				
		// cookies
		assertThat(responseSetCookies.size(), CoreMatchers.is(0));
		
		// old NVPs
		//assertFalse(responseSetCookies.contains(new BasicNameValuePair("Cookie", "Verna-Cookie=chocolate+is+preferred")));
		//assertFalse(responseSetCookies.contains(new BasicNameValuePair("Set-Cookie", "Verna-Cookie=chocolate+is+preferred")));
		
		assertThat(res.getEntity(), CoreMatchers.containsString("HTTP_COOKIE = Verna-Cookie=chocolate+is+preferred"));
	}
	
	//@Ignore
	@Test
	public void requestContainsRawRequest_Headers() {
		printTestName();
		
		Pair head1 = new KeyValuePair("head1", "value1");
		Pair head2 = new KeyValuePair("head2", "value2");
		
		req = new CustomHttpRequestImpl.Builder("legaspi.co", "/php/headers_query.php")
			.proxy(proxy)
			.addHeader(head1.getName(), head1.getValue())
			.addHeader(head2.getName(), head2.getValue())
			//.addHeader("User-Agent", USER_AGENT)
			//.addHeader("Connection", "Close")
			.build();
		
		res = req.submit();
		
		System.out.println( "\ngetRawRequest(): " + ((CustomHttpRequestImpl) req).getRawRequest() );
		
		assertThat(((CustomHttpRequestImpl) req).getRawRequest(), CoreMatchers.containsString(head1.getName() + ": " + head1.getValue()));
		assertThat(((CustomHttpRequestImpl) req).getRawRequest(), CoreMatchers.containsString(head2.getName() + ": " + head2.getValue()));
	}
	
	
	@Test
	public void requestContainsRawRequest_Body() {
		printTestName();

		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair name = new BasicNameValuePair("name", "YoMama and MyMama");
		BasicNameValuePair city = new BasicNameValuePair("city", "Redmond"); 
		BasicNameValuePair zip = new BasicNameValuePair("zip", "AT&T");
		
		formParams.add(name);
		formParams.add(city);
		formParams.add(zip);
		
		ICustomHttpEntity entity = null;
		try {
			entity = new CustomUrlEncodedFormEntity(formParams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req = new CustomHttpRequestImpl.Builder("legaspi.co", "/php/process_name_city_zip.php")
			.proxy(proxy)
			.method(Method.POST)
			.entity(entity)
			.addQueryParam("format", "cli")
			//.addHeader("User-Agent", USER_AGENT)
			//.addHeader("Connection", "Close")
			.build();
		
		res = req.submit();
		
		System.out.println( "\ngetRawRequest(): " + ((CustomHttpRequestImpl) req).getRawRequest() );
		
		assertThat(((CustomHttpRequestImpl) req).getRawRequest(), CoreMatchers.containsString("name=YoMama+and+MyMama&city=Redmond&zip=AT%26T"));
		
	}
	
	
	//@Ignore
	@Test
	public void responseContainsRawRequest_Headers() {
		printTestName();

		Pair head1 = new KeyValuePair("head1", "value1");
		Pair head2 = new KeyValuePair("head2", "value2");
		
		req = new CustomHttpRequestImpl.Builder("legaspi.co", "/php/headers_query.php")
			.proxy(proxy)
			.addHeader(head1.getName(), head1.getValue())
			.addHeader(head2.getName(), head2.getValue())
			//.addHeader("User-Agent", USER_AGENT)
			//.addHeader("Connection", "Close")
			.build();
		
		res = req.submit();
		// magic happens here - setting / transfer of the rawrequest
		((CustomHttpResponseImpl) res).setRawRequest( ((CustomHttpRequestImpl) req).getRawRequest() );
		
		System.out.println( "\nres.getRawRequest(): " + ((CustomHttpResponseImpl) res).getRawRequest() );
		
		assertThat(((CustomHttpResponseImpl) res).getRawRequest(), CoreMatchers.containsString(head1.getName() + ": " + head1.getValue()));
		assertThat(((CustomHttpResponseImpl) res).getRawRequest(), CoreMatchers.containsString(head2.getName() + ": " + head2.getValue()));
	}
	
	
	@Test
	public void responseContainsRawRequest_Body() {
		printTestName();

		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair name = new BasicNameValuePair("name", "YoMama and MyMama");
		BasicNameValuePair city = new BasicNameValuePair("city", "Redmond"); 
		BasicNameValuePair zip = new BasicNameValuePair("zip", "AT&T");
		
		formParams.add(name);
		formParams.add(city);
		formParams.add(zip);
		
		ICustomHttpEntity entity = null;
		try {
			entity = new CustomUrlEncodedFormEntity(formParams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req = new CustomHttpRequestImpl.Builder("legaspi.co", "/php/process_name_city_zip.php")
			.proxy(proxy)
			.method(Method.POST)
			.entity(entity)
			.addQueryParam("format", "cli")
			//.addHeader("User-Agent", USER_AGENT)
			//.addHeader("Connection", "Close")
			.build();
		
		res = req.submit();
		// magic happens here - setting / transfer of the rawrequest
		((CustomHttpResponseImpl) res).setRawRequest( ((CustomHttpRequestImpl) req).getRawRequest() );
		
		System.out.println( "\nres.getRawRequest(): " + ((CustomHttpResponseImpl) res).getRawRequest() );
		
		assertThat(((CustomHttpResponseImpl) res).getRawRequest(), CoreMatchers.containsString("name=YoMama+and+MyMama&city=Redmond&zip=AT%26T"));
		
	}
	
	
	
	/**
	 * Technically - these are already implemented using
	 * CustomHttpRequestImpl_SubmitTest.postNameCityZipWithQueryString
	 */
	@Ignore
	@Test
	public void simplePost200Ok() {
		printTestName();

		fail("not yet impl");
	}
	
	@Ignore
	@Test
	public void simplePost404() {
		printTestName();

		fail("not yet impl");
	}

}
