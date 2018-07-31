package architecture.proxy;

/**
 * 代理接口
 * 
 * @author Lee
 *
 */
public interface Proxy {

	/**
	 * 执行链式代理
	 * 
	 * @param proxyChain
	 * @return
	 * @throws Throwable
	 */
	Object doProxy(ProxyChain proxyChain) throws Throwable;
}
