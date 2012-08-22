package co.legaspi.httptools.test.model;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import co.legaspi.httptools.model.Protocol;
import co.legaspi.httptools.model.ProxyInfo;

public class ProxyInfoDefaultsTest {
	
	
	ProxyInfo proxy;

	@Before
	public void setUp() throws Exception {
		proxy = new ProxyInfo();
	}

	@Test
	public void proxyInfoNotNullByDefault() {
		System.out.println(proxy.toString());
		assertThat(proxy, CoreMatchers.notNullValue());
	}
	
	@Test
	public void proxyDisabledByDefault() {
		assertThat(proxy.isProxyEnabled(), CoreMatchers.is(false));
	}
	
	@Test
	public void proxyNotNullButStillDisabledAfterConstructor() {
		ProxyInfo proxy = new ProxyInfo(Protocol.HTTP, "localhost", 8888);
		System.out.println("proxy.toString(): " + proxy.toString());
		assertNotNull(proxy);
		assertThat(proxy.isProxyEnabled(), CoreMatchers.is(false));
	}

}
