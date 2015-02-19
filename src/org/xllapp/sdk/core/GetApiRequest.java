package org.xllapp.sdk.core;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xllapp.sdk.core.util.ReflectionUtils;

/**
 * 使用get方式发送的http类型的请求.
 *
 * @author dylan.chen Sep 25, 2014
 *
 */
public abstract class GetApiRequest extends HttpApiRequest {

	public GetApiRequest(ApiClient apiClient) {
		super(apiClient);
	}

	@Override
	public HttpMethod getMethod() {
		return HttpMethod.GET;
	}

	public Map<String, String> getRequestParams() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		ReflectionUtils.doWithObjectFields(this.getClass(), true, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Object fieldValue=ReflectionUtils.getFieldValue(GetApiRequest.this, field);
				if (null != fieldValue) {
					map.put(ReflectionUtils.getFieldName(field, true), fieldValue.toString());
				}
			}
		});
		map.put("sign", this.getSign());
		return map;
	}

}
