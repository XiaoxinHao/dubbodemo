package com.newidor.demo.dubbo.provider;

import java.util.concurrent.atomic.AtomicInteger;

import com.newidor.demo.dubbo.api.CacheService;

public class CacheServiceImpl implements CacheService {

	// AtomicInteger通过一种线程安全的加减操作接口
	private final AtomicInteger i = new AtomicInteger();

	public String findCache(String id) {
		String result = "request: " + id + ", response: " + i.getAndIncrement();
		System.out.println(result);
		return result;
	}

}
