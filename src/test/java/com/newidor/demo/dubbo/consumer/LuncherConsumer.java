package com.newidor.demo.dubbo.consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LuncherConsumer {
	public static void main(String[] args) throws InterruptedException {
		LuncherConsumer luncher = new LuncherConsumer();
		luncher.start();
	}

	void start() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[]{"dubbo-demo-consumer.xml","dubbo-demo-action.xml"});
		DemoAction demoAction = (DemoAction) context.getBean("demoAction");
		try {
			demoAction.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}