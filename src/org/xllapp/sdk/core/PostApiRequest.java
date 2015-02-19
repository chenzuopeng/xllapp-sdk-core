package org.xllapp.sdk.core;

import org.apache.http.HttpEntity;

/**
 * 使用post方式发送的http类型的请求.
 *
 * @author dylan.chen Sep 25, 2014
 *
 */
public abstract class PostApiRequest extends HttpApiRequest {

	public PostApiRequest(ApiClient apiClient) {
		super(apiClient);
	}

	protected abstract HttpEntity getEntity();

	@Override
	public HttpMethod getMethod() {
		return HttpMethod.POST;
	}

}
