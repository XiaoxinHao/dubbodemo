/**
 *Copyright 1999-2011 Alibaba Group.
   
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
   
  http://www.apache.org/licenses/LICENSE-2.0
   
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.newidor.demo.dubbo.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.alibaba.dubbo.rpc.RpcContext;
import com.newidor.demo.dubbo.api.CacheService;
import com.newidor.demo.dubbo.api.CallbackListener;
import com.newidor.demo.dubbo.api.CallbackService;
import com.newidor.demo.dubbo.api.DemoService;
import com.newidor.demo.dubbo.api.NIOService;
import com.newidor.demo.dubbo.api.Person;

public class DemoAction {

	private DemoService demoService;
	private CacheService cacheService;
	private CallbackService callbackService;
	private NIOService nioService;

	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}
	public void setCallbackService(CallbackService callbackService) {
		this.callbackService = callbackService;
	}
	public void setNioService(NIOService nioService) {
		this.nioService = nioService;
	}
	
	
	public void start() throws Exception {
		//testSimple();

		//testCache();

		testCallback();
		
		//testNIO();
	}

	public void testSimple() throws InterruptedException {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				String hello = demoService.sayHello("world" + i);
				System.out.println("["
						+ new SimpleDateFormat("HH:mm:ss").format(new Date())
						+ "] " + hello);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(2000);
		}
	}

	public void testCache() throws InterruptedException {

		// 测试缓存生效，多次调用返回同样的结果。(服务器端自增长返回值)
		String fix = null;
		for (int i = 0; i < 5; i++) {
			String result = cacheService.findCache("0");
			if (fix == null || fix.equals(result)) {
				System.out.println("i=" + i + " OK: " + result);
			} else {
				System.err.println("i=" + i + " ERROR: " + result);
			}
			fix = result;
			Thread.sleep(500);
		}

		// LRU的缺省cache.size为1000，执行1001次，应有溢出
		for (int n = 0; n < 1001; n++) {
			String pre = null;
			for (int i = 0; i < 10; i++) {
				String result = cacheService.findCache(String.valueOf(n));
				if (pre != null && !pre.equals(result)) {
					System.err.println("n=" + n + " ERROR: " + result);
				}
				pre = result;
			}
		}

		// 测试LRU有移除最开始的一个缓存项
		String result = cacheService.findCache("0");
		if (fix != null && !fix.equals(result)) {
			System.out.println("OK: " + result);
		} else {
			System.err.println("ERROR:" + result);
		}
	}

	public void testCallback() throws InterruptedException {
		callbackService.addListener("hanshubo", new CallbackListener() {
			public void changed(String msg) {
				System.out.println("callback1:" + msg);
			}
		});

		callbackService.addListener("hanyiyi", new CallbackListener() {
			public void changed(String msg) {
				System.out.println("callback2:" + msg);
			}
		});

		while (true) {
			Thread.sleep(1000);
		}
	}

	
	public void testNIO() throws InterruptedException, ExecutionException{
        Person p = nioService.getPersion("hanshubo");
        System.out.println(p);
        
        //如果dubbo-demo-consumer.xml中配置为<dubbo:reference id="nioService" interface="com.alibaba.dubbo.demo.NIOService" async="false"/>
        //则上述代码能直接打印出p，如下代码则报错
        Future<Person> pFuture = RpcContext.getContext().getFuture();
 
        p = pFuture.get();
        System.out.println(p);
		
	}
}