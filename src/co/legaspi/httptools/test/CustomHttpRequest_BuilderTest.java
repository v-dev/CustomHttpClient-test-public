package co.legaspi.httptools.test;

import org.hamcrest.CoreMatchers;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;

import co.legaspi.httptools.CustomHttpRequestImpl;
import co.legaspi.httptools.HelperMethods;
import co.legaspi.httptools.model.CustomHttpEntity;
import co.legaspi.httptools.model.CustomStringEntity;
import co.legaspi.httptools.model.Header;
import co.legaspi.httptools.model.Headers;
import co.legaspi.httptools.model.ICustomHttpEntity;
import co.legaspi.httptools.model.KeyValuePair;
import co.legaspi.httptools.model.Method;
import co.legaspi.httptools.model.Parameters;
import co.legaspi.httptools.model.Protocol;
import co.legaspi.httptools.model.ProxyInfo;

public class CustomHttpRequest_BuilderTest {
	private static final String HOST = "legaspi.co";
	private static final String PATH = "/php/headers_query.php";
	
	private static final String queryParamKey1 = "id";
	private static final String queryParamKey2 = "app";
	
	private static ICustomHttpEntity entity;
	private static final String entityBody = HelperMethods.randomLipsum();
	
	CustomHttpRequestImpl req;
	
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Rule
	public TestName name = new TestName(); 
	
	@BeforeClass
	public static void beforeClass() throws UnsupportedEncodingException {
		entity = new CustomStringEntity(entityBody);
	}
	
	private void printTestName() {
		System.out.println("\n********** TEST: " + name.getMethodName() + " **********");
	}

	@Test
	public void constructorSetsHostAndPath() {
		printTestName();
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).build();
		assertConstructorSetsHostAndPath();
	}
	
	private void assertConstructorSetsHostAndPath() {
		assertThat(req.getHost(), CoreMatchers.equalTo(HOST));
		assertThat(req.getPath(), CoreMatchers.equalTo(PATH));
	}
	
	@Test
	public void optionalFieldsSetToDefault_Protocol() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertThat(req.getProtocol(), CoreMatchers.equalTo(Protocol.HTTP));
	}
	
	@Test
	public void optionalFieldsSetToDefault_Method() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertThat(req.getMethod(), CoreMatchers.equalTo(Method.GET));
	}
	
	@Test
	public void optionalFieldsSetToDefault_Port() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertThat(req.getPort(), CoreMatchers.equalTo(80));
	}
	
	@Test
	public void optionalFieldsSetToDefault_Proxy() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertDefaultProxyInfo();
	}
	
	private void assertDefaultProxyInfo() {
		printTestName();
		
		System.out.println(req.getProxy().toString());
		assertThat(req.getProxy().getProxyProtocol(), CoreMatchers.equalTo(Protocol.HTTP));
		assertThat(req.getProxy().getProxyHost(), CoreMatchers.equalTo(""));
		assertThat(req.getProxy().getProxyPort(), CoreMatchers.equalTo(8080));
		assertThat(req.getProxy().isProxyEnabled(), CoreMatchers.equalTo(false));
	}
	
	@Test
	public void optionalFieldsSetToDefault_Header() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertThat(req.getHeaders().size(), CoreMatchers.is(0));
	}
	
	@Test
	public void optionalFieldsSetToDefault_QueryParams() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertThat(req.getQueryParams().size(), CoreMatchers.is(0));
	}
	
	//@Test (expected=IllegalStateException.class)
	@Test
	public void optionalFieldsSetToDefault_Entity() throws Exception {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				build();
		assertConstructorSetsHostAndPath();
		assertDefaultEntity();
	}
	
	private void assertDefaultEntity() throws Exception {
		expectedEx.expect(IllegalStateException.class);
		expectedEx.expectMessage("Content has not been provided");
		req.getEntity().getContent();
	}
	
	@Test
	public void optionalFieldsSetToDefault_CookieJar() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				entity(new CustomHttpEntity()).
				build();
		assertConstructorSetsHostAndPath();
		assertThat(req.getCookieJar(), CoreMatchers.notNullValue());
	}
	
	@Test
	public void noArgsDefaults_Proxy() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				proxy(new ProxyInfo()).
				build();
		assertConstructorSetsHostAndPath();
		assertDefaultProxyInfo();
	}
	
	@Test
	public void noArgsDefaults_Entity() throws Exception {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				entity(new CustomHttpEntity()).
				build();
		assertDefaultEntity();
	}
	
	@Test
	public void methodCanBeSetToPost() {
		printTestName();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				method(Method.POST).
				build();
		assertConstructorSetsHostAndPath();
		
		assertThat(req.getMethod(), CoreMatchers.equalTo(Method.POST));
	}
	
	@Test
	public void proxy() {
		printTestName();
		
		ProxyInfo proxy = setupProxy();
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				proxy(proxy).
				build();
		assertConstructorSetsHostAndPath();
		
		assertThat(req.getProxy(), CoreMatchers.equalTo(proxy));
		assertThat(req.getProxy().isProxyEnabled(), CoreMatchers.is(true));
	}
	
	private static ProxyInfo setupProxy() {
		ProxyInfo proxy = new ProxyInfo(Protocol.HTTPS, "localhost", 8888);
		proxy.enableProxy();
		return proxy;
	}
	
	@Test
	public void addHeadersOneAtATime() {
		printTestName();
		
		Headers headers = setupHeaders();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				addHeader(headers.getByIndex(0).getName(), headers.getByIndex(0).getValue()).
				addHeader(headers.getByIndex(1).getName(), headers.getByIndex(1).getValue()).
				build();
		assertConstructorSetsHostAndPath();
		
		assertHeaders(headers);
	}
	
	@Test
	public void addHeadersAsList() {
		printTestName();
		
		Headers headers = setupHeaders();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				headers(headers).
				build();
		assertConstructorSetsHostAndPath();
		
		assertHeaders(headers);
	}
	
	private static Headers setupHeaders() {
		Headers headers = new Headers();
		headers.add(new Header("Cookie", "yummy"));
		headers.add(new Header("AppId", "MyApp"));
		return headers;
	}
	
	private void assertHeaders(Headers headers) {
		
		System.out.println(headers);
		System.out.println("req: " + req.getHeaders().getByKey("Cookie"));
		System.out.println("req: " + req.getHeaders().getByKey("AppId"));
		
		System.out.println("headers: " + headers.getByKey("Cookie"));
		System.out.println("headers: " + headers.getByKey("AppId"));
		
		//assertThat( req.getHeaders(), CoreMatchers.equalTo(headers) );
		
		//assertThat(req.getHeaders().getByKey("Cookie"), CoreMatchers.equalTo(headers.getByKey("Cookie")));
		//assertThat(req.getHeaders().getByKey("AppId"), CoreMatchers.equalTo(headers.getByKey("AppId")));
		assertThat(req.getHeaders().getByKey("Cookie").getByIndex(0).getValue(), CoreMatchers.equalTo(headers.getByKey("Cookie").getByIndex(0).getValue()));
		assertThat(req.getHeaders().getByKey("AppId").getByIndex(0).getValue(), CoreMatchers.equalTo(headers.getByKey("AppId").getByIndex(0).getValue()));
		
		assertThat(req.getHeaders().getByKey("Cookie").getByIndex(0).getName(), CoreMatchers.equalTo("Cookie"));
		assertThat(req.getHeaders().getByKey("AppId").getByIndex(0).getName(), CoreMatchers.equalTo("AppId"));
	}
	
	@Test
	public void queryParams() {
		printTestName();
		
		Parameters qps = setupQueryParams();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				addQueryParam(qps.getByIndex(0).getName(), qps.getByIndex(0).getValue()).
				addQueryParam(qps.getByIndex(1).getName(), qps.getByIndex(1).getValue()).
				build();
		assertConstructorSetsHostAndPath();
		
		assertQueryParams(qps);
	}
	
	private static Parameters setupQueryParams() {
		Parameters qp = new Parameters();
		qp.add(new KeyValuePair("id", "1234"));
		qp.add(new KeyValuePair("app", "yourApp"));
		return qp;
	}
	
	private void assertQueryParams(Parameters qps) {
		//assertThat(req.getQueryParams().getByKey(queryParamKey1), CoreMatchers.equalTo(qps.getByKey(queryParamKey1)));
		//assertThat(req.getQueryParams().getByKey(queryParamKey2), CoreMatchers.equalTo(qps.getByKey(queryParamKey2)));
		
		assertThat(req.getQueryParams().getByKey(queryParamKey1).getByIndex(0).getValue(), CoreMatchers.equalTo(qps.getByKey(queryParamKey1).getByIndex(0).getValue()));
		assertThat(req.getQueryParams().getByKey(queryParamKey2).getByIndex(0).getValue(), CoreMatchers.equalTo(qps.getByKey(queryParamKey2).getByIndex(0).getValue()));
	}
	
	@Test
	public void proxyAndQueryParamsNotDefault() {
		printTestName();
		
		ProxyInfo proxy = setupProxy();
		Parameters queryParams = setupQueryParams();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				proxy(proxy).
				addQueryParam(queryParams.getByIndex(0).getName(), queryParams.getByIndex(0).getValue()).
				addQueryParam(queryParams.getByIndex(1).getName(), queryParams.getByIndex(1).getValue()).
				build();
		assertConstructorSetsHostAndPath();
		assertQueryParams(queryParams);
		assertProxyInfo(proxy);
	}
	
	private void assertProxyInfo(ProxyInfo proxy) {
		assertThat(req.getProxy(), CoreMatchers.equalTo(proxy));
		assertThat(req.getProxy().isProxyEnabled(), CoreMatchers.is(true));
	}
	
	//TODO: probably make all my other setupXXX helper methods like this - just use a class-wide variable
	@Test
	public void headersAndEntityNotDefault() {
		printTestName();
		
		Headers headers = setupHeaders();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				addHeader(headers.getByIndex(0).getName(), headers.getByIndex(0).getValue()).
				addHeader(headers.getByIndex(1).getName(), headers.getByIndex(1).getValue()).
				entity(entity).
				build();
		assertConstructorSetsHostAndPath();
		assertHeaders(headers);
		assertEntity();
	}
	
	private void assertEntity() {
		//System.out.println("\nassertEntity");
		ICustomHttpEntity se = req.getEntity();
		/*String entityString = "";
		try {
			entityString = EntityUtils.toString(se);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		//assertThat(se, CoreMatchers.isA(StringEntity.class));
		//assertThat(se, CoreMatchers.equalTo(entity));
		assertThat(se, CoreMatchers.is(entity));
		
		//System.out.println("entityString: " + entityString);
		System.out.println("  entityBody: " + entityBody);
		
		//fail("Need to verify complete coverage here for entities.");
	}
	
	@Test
	public void proxyHeadersParamsAndEntityNotDefault() {
		printTestName();
		
		ProxyInfo proxy = setupProxy();
		Headers headers = setupHeaders();
		Parameters queryParams = setupQueryParams();
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				proxy(proxy).
				addHeader(headers.getByIndex(0).getName(), headers.getByIndex(0).getValue()).
				addHeader(headers.getByIndex(1).getName(), headers.getByIndex(1).getValue()).
				addQueryParam(queryParams.getByIndex(0).getName(), queryParams.getByIndex(0).getValue()).
				addQueryParam(queryParams.getByIndex(1).getName(), queryParams.getByIndex(1).getValue()).
				entity(entity).
				build();
		assertConstructorSetsHostAndPath();
		
		assertProxyInfo(proxy);
		assertQueryParams(queryParams);
		assertHeaders(headers);
		assertEntity();
	}
	
	@Test
	public void portocolAndPortNotDefault() {
		printTestName();
		
		int myPort = 8888;
		
		req = new CustomHttpRequestImpl.Builder(HOST, PATH).
				protocol(Protocol.HTTPS).
				port(myPort).
				build();
		
		assertThat(req.getProtocol(), CoreMatchers.equalTo(Protocol.HTTPS));
		assertThat(req.getPort(), CoreMatchers.equalTo(myPort));
	}
	

}
