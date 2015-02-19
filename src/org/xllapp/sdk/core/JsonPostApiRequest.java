package org.xllapp.sdk.core;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.xllapp.sdk.core.util.JsonUtils;

/**
 *
 * Json请求.
 *
 * @author dylan.chen Sep 23, 2014
 *
 */
public abstract class JsonPostApiRequest extends PostApiRequest {

	public JsonPostApiRequest(ApiClient apiClient) {
		super(apiClient);
	}

	@Override
	public HttpEntity getEntity() {
		HttpEntity entity = null;
		try {
			entity = new StringEntity(getJson(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	public String getJson(){
		JSONObject jsonObject=JsonUtils.toJSONObject(this);
		try {
			jsonObject.put("sign", this.getSign());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonObject.toString();
	}

}
