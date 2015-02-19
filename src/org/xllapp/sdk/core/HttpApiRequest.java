package org.xllapp.sdk.core;

/**
 * http请求.
 *
 * @author dylan.chen Sep 26, 2014
 *
 */
public abstract class HttpApiRequest extends ApiRequest {


	public HttpApiRequest(ApiClient apiClient) {
		super(apiClient);
	}

	public String getUrl() {
		String server = this.getApiConfig().getServer();
		String uri = this.getUri();
		if((!server.endsWith("/")) && (!uri.startsWith("/"))){
          return server + "/" + uri;
		}
		return server + uri;
	}

	public abstract String getUri();

	public abstract HttpMethod getMethod();

	public enum HttpMethod{
		GET,POST;
	}

}
