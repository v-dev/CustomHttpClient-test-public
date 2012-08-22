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

import co.legaspi.httptools.CustomHttpRequestImpl;
import co.legaspi.httptools.CustomHttpResponse;
import co.legaspi.httptools.model.CustomUrlEncodedFormEntity;
import co.legaspi.httptools.model.ICustomHttpEntity;
import co.legaspi.httptools.model.Method;
import co.legaspi.httptools.model.Protocol;
import co.legaspi.httptools.model.ProxyInfo;

public class CustomHttpRequestImpl_SubmitTest {
	private static final String HOST = "legaspi.co";
	private static final String GET_PATH = "/php/headers_query.php";
	private static final String POST_PATH = "/php/process_name_city_zip.php";
	private static final String MSISDN = "1234567890";
	
	private static final String PROXY_HOST = "localhost";
	private static final int PROXY_PORT = 8888;
	
	private static final ProxyInfo proxy = new ProxyInfo(Protocol.HTTP, PROXY_HOST, PROXY_PORT);
	
	private static CustomHttpRequestImpl req;
	
	@Rule
	public TestName name = new TestName(); 
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}
	
	@BeforeClass
	public static void beforeClass() {
		//proxy.enableProxy();
		proxy.disableProxy();
	}
	
	@Test
	public void getWithQueryParam() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, GET_PATH).
				addHeader("x-a-phone-number", MSISDN).
				proxy(proxy).
				addQueryParam("format", "cli").
				build();
		
		CustomHttpResponse response = req.submit();
		String res = response.getRaw();
		
		System.out.println("\nres: " + res);
		
		assertThat(res, CoreMatchers.startsWith("HTTP/1.1 200 OK"));
		//assertThat(response.getStatusLine().getStatusCode(), CoreMatchers.is(200));
		assertThat(res, CoreMatchers.containsString("HTTP_"));
		assertThat(res, CoreMatchers.not(CoreMatchers.containsString("<br />")));
	}
	
	//@Ignore
	@Test
	public void getWithOutQueryParam() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, GET_PATH).
				addHeader("x-a-phone-number", MSISDN).
				proxy(proxy).
				build();
		
		CustomHttpResponse response = req.submit();
		String rawRes = response.getRaw();

		System.out.println("\nres: " + rawRes);
		
		assertThat(rawRes, CoreMatchers.startsWith("HTTP/1.1 200 OK"));
		assertThat(rawRes, CoreMatchers.containsString("HTTP_"));
		assertThat(rawRes, CoreMatchers.containsString("<br />"));
	}
	
	
	//@Ignore
	@Test
	public void getSslWithOutQueryParam() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, GET_PATH).
				addHeader("x-up-calling-line-id", MSISDN).
				proxy(proxy).
				protocol(Protocol.HTTPS).
				port(443).
				build();
		
		CustomHttpResponse response = req.submit();
		String rawRes = response.getRaw();

		System.out.println("\nres: " + rawRes);
		
		assertThat(rawRes, CoreMatchers.startsWith("HTTP/1.1 200 OK"));
		assertThat(rawRes, CoreMatchers.containsString("HTTP_"));
		assertThat(rawRes, CoreMatchers.containsString("<br />"));
	}

	@Test
	public void postNameCityZipWithQueryString() {
		printTestName();
		
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair name = new BasicNameValuePair("name", "YoMama and MyMama");
		BasicNameValuePair city = new BasicNameValuePair("city", "Redmond"); 
		BasicNameValuePair zip = new BasicNameValuePair("zip", "AT&T");
		
		formParams.add(name);
		formParams.add(city);
		formParams.add(zip);
		
		/*Cookie cookie = new BasicClientCookie("verna_field", "was_here");
		CookieStore cookieJar = new BasicCookieStore();
		cookieJar.addCookie(cookie);*/
		
		ICustomHttpEntity entity = null;
		try {
			entity = new CustomUrlEncodedFormEntity(formParams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req = new CustomHttpRequestImpl.Builder(HOST, POST_PATH).
				method(Method.POST).
				addQueryParam("format", "cli").
				proxy(proxy).
				entity(entity).
				//cookieJar(cookieJar).
				build();
		
		String rawResponse = req.submit().getRaw();
		System.out.println("\npostNameCityZip res: " + rawResponse);
		
		assertThat(rawResponse, CoreMatchers.containsString("Name: " + name.getValue()));
		assertThat(rawResponse, CoreMatchers.containsString("City: " + city.getValue()));
		assertThat(rawResponse, CoreMatchers.containsString("ZIP: "  + zip.getValue()));
	}
	
	//@Ignore
	@Test
	public void postSslNameCityZipWithQueryString() {
		printTestName();
		
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair name = new BasicNameValuePair("name", "YoMama and MyMama");
		BasicNameValuePair city = new BasicNameValuePair("city", "Redmond"); 
		BasicNameValuePair zip = new BasicNameValuePair("zip", "AT&T");
		
		formParams.add(name);
		formParams.add(city);
		formParams.add(zip);
		
		/*Cookie cookie = new BasicClientCookie("verna_field", "was_here");
		CookieStore cookieJar = new BasicCookieStore();
		cookieJar.addCookie(cookie);*/
		
		ICustomHttpEntity entity = null;
		try {
			entity = new CustomUrlEncodedFormEntity(formParams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req = new CustomHttpRequestImpl.Builder(HOST, POST_PATH).
				proxy(proxy).
				method(Method.POST).
				addQueryParam("format", "cli").
				proxy(proxy).
				protocol(Protocol.HTTPS).
				port(443).
				entity(entity).
				//cookieJar(cookieJar).
				build();
		
		String rawResponse = req.submit().getRaw();
		System.out.println("\npostNameCityZip res: " + rawResponse);
		
		assertThat(rawResponse, CoreMatchers.containsString("Name: " + name.getValue()));
		assertThat(rawResponse, CoreMatchers.containsString("City: " + city.getValue()));
		assertThat(rawResponse, CoreMatchers.containsString("ZIP: "  + zip.getValue()));
	}
	
	
	@Test
	public void disableSslHostnameCheck_True_http() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, GET_PATH).
				disableSslHostnameCheck(true).
				proxy(proxy).
				addQueryParam("format", "cli").
				build();
		
		CustomHttpResponse response = req.submit();
		
		System.out.println("request: " + req.getRawRequest());
		System.out.println("\n\nresponse: " + response.getRaw());
		
		assertThat(response.getRaw(), CoreMatchers.containsString("HTTP_HOST = legaspi.co:80"));
		assertThat(response.getRaw(), CoreMatchers.containsString("[format] => cli"));
	}
	
	@Test
	public void disableSslHostnameCheck_True_https() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, GET_PATH).
				disableSslHostnameCheck(true).
				protocol(Protocol.HTTPS).
				port(443).
				proxy(proxy).
				addQueryParam("format", "cli").
				build();
		
		CustomHttpResponse response = req.submit();
		
		System.out.println("request: " + req.getRawRequest());
		System.out.println("\n\nresponse: " + response.getRaw());
		
		assertThat(response.getRaw(), CoreMatchers.containsString("HTTP_HOST = legaspi.co:443"));
		assertThat(response.getRaw(), CoreMatchers.containsString("[format] => cli"));
	}
	
}
