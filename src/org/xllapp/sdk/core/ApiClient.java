package org.xllapp.sdk.core;

import org.apache.http.Header;
import org.json.JSONObject;
import org.xllapp.sdk.core.BuildConfig;
import org.xllapp.sdk.core.HttpApiRequest.HttpMethod;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

/**
 *
 * 此类用于向服务端发送请求.
 *
 * @author dylan.chen Sep 23, 2014
 *
 */
public class ApiClient {

	private ApiConfig apiConfig;

	private AsyncHttpClient asyncHttpClient;

	private SyncHttpClient syncHttpClient;

	public static ApiClient getApiClient(ApiConfig apiConfig) {
		return new ApiClient(apiConfig);
	}

	private ApiClient(ApiConfig apiConfig) {
		this.apiConfig = apiConfig;
		initAsyncHttpClient();
		initSyncHttpClient();
	}

	private void initAsyncHttpClient() {
		this.asyncHttpClient = new AsyncHttpClient();
		this.asyncHttpClient.setConnectTimeout(this.apiConfig.getConnectTimeout());
		this.asyncHttpClient.setResponseTimeout(this.apiConfig.getResponseTimeout());
	}

	private void initSyncHttpClient() {
		this.syncHttpClient = new SyncHttpClient();
		this.syncHttpClient.setConnectTimeout(this.apiConfig.getConnectTimeout());
		this.syncHttpClient.setResponseTimeout(this.apiConfig.getResponseTimeout());
	}

	public ApiResponse<?> sendSyncRequest(ApiRequest request) throws Exception {
		final Object[] returnObject = new Object[1];
		sendRequest(this.syncHttpClient, request, new ResponseListener() {

			@Override
			public void onSuccess(ApiResponse<?> response) {
				returnObject[0] = response;
			}

			@Override
			public void onFailure(Throwable throwable) {
				returnObject[0] = throwable;
			}

		});

		Object result = returnObject[0];
		if (result instanceof Throwable) {
			throw new Exception((Throwable) result);
		} else {
			return (ApiResponse<?>) result;
		}
	}

	public void sendAsyncRequest(ApiRequest request, ResponseListener responseListener) throws Exception {
		sendRequest(this.asyncHttpClient, request, responseListener);
	}

	private void sendRequest(AsyncHttpClient httpClient, final ApiRequest request, final ResponseListener responseListener) {

		if (BuildConfig.DEBUG) {
			Log.d(ApiClient.class.getName(), "request:" + request);
		}

		ResponseHandlerInterface responseHandler = new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

				if (BuildConfig.DEBUG) {
					Log.d(ApiClient.class.getName(), "response json:" + jsonObject);
				}

				ApiResponse<?> response = request.getResponse(jsonObject);

				if (BuildConfig.DEBUG) {
					Log.d(ApiClient.class.getName(), "response object:" + response);
				}

				responseListener.onSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				responseListener.onFailure(throwable);
			}

		};

		if (request instanceof HttpApiRequest) {
			HttpApiRequest httpApiRequest = (HttpApiRequest) request;
			String url = httpApiRequest.getUrl();

			if (BuildConfig.DEBUG) {
				Log.d(ApiClient.class.getName(), "api url:" + url);
			}

			if (HttpMethod.POST == httpApiRequest.getMethod()) {
				PostApiRequest postApiRequest = (PostApiRequest) request;
				httpClient.post(null, url, postApiRequest.getEntity(), RequestParams.APPLICATION_JSON, responseHandler);
			} else {
				GetApiRequest getApiRequest = (GetApiRequest) request;
				httpClient.get(url, new RequestParams(getApiRequest.getRequestParams()), responseHandler);
			}
		} else {
			throw new RuntimeException("unsupported request type[" + request.getClass().getName() + "]");
		}

	}

	public ApiConfig getApiConfig() {
		return this.apiConfig;
	}

}
