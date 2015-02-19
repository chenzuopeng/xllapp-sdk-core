package org.xllapp.sdk.core;

/**
*
* 响应回调接口.用于异步执行的请求完成时,返回响应信息.
*
* @author dylan.chen Sep 23, 2014
*
*/
public interface ResponseListener {

	public void onSuccess(ApiResponse<?> response);

	public void onFailure(Throwable throwable);

}
