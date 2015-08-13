package com.newidor.demo.dubbo.api;

public interface CallbackService {

	void addListener(String key, CallbackListener listener);

}
