package org.xllapp.sdk.core;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xllapp.sdk.core.util.JsonUtils;
import org.xllapp.sdk.core.util.ToStringBuilder;

/**
 * 响应基类.
 *
 * @author dylan.chen Sep 23, 2014
 *
 */
public abstract class ApiResponse<T> {

	private String requestId;

	private ResultCode resultCode;

	private String resultDesc;

	private T data;

	private String timestamp;

	@SuppressWarnings("unchecked")
	public ApiResponse(JSONObject jsonObject) {
		try {
            this.requestId = jsonObject.getString("request_id");
			this.resultCode = jsonObject.getInt("result_code") == 0 ? ResultCode.SUCCESS : ResultCode.FAILURE;
			this.resultDesc = jsonObject.getString("result_desc");
			this.timestamp = jsonObject.getString("timestamp");

			if (!jsonObject.isNull("data")) {
				Object dataJsonValue = jsonObject.get("data");
				Class<T> dataType = getDataType();
				Object dataValue = null;
				if (dataType.isArray()) {
					if (dataJsonValue instanceof JSONArray) {
						dataValue = JsonUtils.toArray((JSONArray) dataJsonValue, dataType.getComponentType());
					} else {
						throw new RuntimeException("expected json type[org.json.JSONArray], actual json type:" + dataJsonValue.getClass().getName());
					}
				} else {
					if (dataJsonValue instanceof JSONObject) {
						dataValue = JsonUtils.toObject((JSONObject) dataJsonValue, dataType);
					} else {
						throw new RuntimeException("expected json type[org.json.JSONObject], actual json type:" + dataJsonValue.getClass().getName());
					}
				}
				setData((T) dataValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	protected Class getDataType() {
		Type genType = this.getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Type genericClass = params[0];
		if (genericClass instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) genericClass).getGenericComponentType();
			if (componentType instanceof Class) {
				return Array.newInstance((Class) componentType, 0).getClass();
			} else {
				throw new RuntimeException("unsupported genericType:" + Arrays.toString(params));
			}
		} else if (genericClass instanceof Class) {
			return (Class) genericClass;
		} else {
			throw new RuntimeException("unsupported genericType:" + Arrays.toString(params));
		}
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ResultCode getResultCode() {
		return this.resultCode;
	}

	protected void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return this.resultDesc;
	}

	protected void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public T getData() {
		return this.data;
	}

	protected void setData(T data) {
		this.data = data;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	protected void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("requestId", this.requestId);
		map.put("resultCode", this.resultCode.name());
		map.put("resultDesc", this.resultDesc);
		map.put("timestamp", this.timestamp);
		map.put("data", ToStringBuilder.toString(this.data));
		return map.toString();
	}

	/**
	 * 结果码枚举.
	 */
	public enum ResultCode {

		SUCCESS, FAILURE;

	}

}
