package org.xllapp.sdk.core;

/**
 *
* 配置.
*
* @author dylan.chen Sep 23, 2014
*
*/
public abstract class ApiConfig {

	/**
	 * 默认的连接超时时长:10秒
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;

	/**
	 * 默认的响应超时时长：10秒
	 */
	public static final int DEFAULT_RESPONSE_TIMEOUT = 10 * 1000;

	/**
	 * 测试环境的服务器地址
	 */
	public static final String SERVER_TEST="";

	/**
	 * 生产环境的服务器地址
	 */
	public static final String SERVER_PRODUCT="";

	/**
	 * 获取连接超时时长,单位:毫秒.
	 */
	public int getConnectTimeout() {
		return DEFAULT_CONNECT_TIMEOUT;
	}

	/**
	 * 获取响应超时时长,单位:毫秒.
	 */
	public int getResponseTimeout() {
		return DEFAULT_RESPONSE_TIMEOUT;
	}

	public abstract String getServer();

	public abstract String getDes3Key();

	public abstract String getAppKey();

	/**
	 * 获取产品ID
	 */
	public abstract String getProductId();

	/**
	 * 获取客户端类型
	 */
	public abstract String getClientType();

	/**
	 * 获取客户端版本
	 */
	public abstract String getClientVersion();

	/**
	 * 获取客户端渠道类型
	 */
	public abstract String getClientChannelType();

	/**
	 * 获取客户端操作系统类型
	 */
	public String getOsType() {
		return "android";
	}

	/**
	 * 获取imsi
	 */
	public abstract String getImsi();

	/**
	 * 获取imei
	 */
	public abstract String getImei();

}
