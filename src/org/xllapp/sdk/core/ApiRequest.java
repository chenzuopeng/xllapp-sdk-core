package org.xllapp.sdk.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.xllapp.sdk.core.util.SignUtils;
import org.xllapp.sdk.core.util.ToStringBuilder;
import org.xllapp.sdk.core.util.ReflectionUtils.FieldIgnore;

import android.annotation.SuppressLint;

/**
 * 请求基类.
 *
 * @author dylan.chen Sep 23, 2014
 *
 */
public abstract class ApiRequest {

	@FieldIgnore
	private ApiConfig apiConfig;

	private String productId;

	private String orgCode;

	private String cityCode;

	private String clientType;

	private String clientVersion;

	private String clientChannelType;

	private String osType;

	private String imsi;

	private String imei;

	private String mobile;

	private String longitude;

	private String latitude;

	private String timestamp;

	public ApiRequest(ApiClient apiClient) {
		this.apiConfig = apiClient.getApiConfig();
		this.productId = this.apiConfig.getProductId();
		this.clientType = this.apiConfig.getClientType();
		this.clientVersion = this.apiConfig.getClientVersion();
		this.clientChannelType = this.apiConfig.getClientChannelType();
		this.osType = this.apiConfig.getOsType();
		this.imsi = this.apiConfig.getImsi();
		this.imei = this.apiConfig.getImei();

		this.timestamp = getTimestampAsString();
	}

	protected String getSign() {
		String[] signItems = getSignItems();
		String[] fullSignItems = null;
		if (null != signItems && signItems.length > 0) {
			fullSignItems = new String[signItems.length + 2];
			System.arraycopy(signItems, 0, fullSignItems, 0, signItems.length);
		} else {
			fullSignItems = new String[2];
		}
		String timestamp = this.timestamp;
		fullSignItems[fullSignItems.length - 2] = this.apiConfig.getAppKey();
		fullSignItems[fullSignItems.length - 1] = timestamp;
		return SignUtils.generateSign(fullSignItems, timestamp, this.apiConfig.getDes3Key());
	}

	public abstract String[] getSignItems();

	public abstract ApiResponse<?> getResponse(JSONObject jsonObject);

	public ApiConfig getApiConfig() {
		return apiConfig;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	@SuppressLint("SimpleDateFormat")
	protected String getTimestampAsString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getClientVersion() {
		return this.clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getClientChannelType() {
		return this.clientChannelType;
	}

	public void setClientChannelType(String clientChannelType) {
		this.clientChannelType = clientChannelType;
	}

	public String getOsType() {
		return this.osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getImsi() {
		return this.imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		Map<String, String> map = ToStringBuilder.toMap(this);
		map.put("sign", this.getSign());
		return map.toString();
	}

}
