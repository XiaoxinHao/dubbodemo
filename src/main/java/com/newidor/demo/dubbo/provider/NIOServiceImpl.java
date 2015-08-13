package com.newidor.demo.dubbo.provider;

import com.newidor.demo.dubbo.api.NIOService;
import com.newidor.demo.dubbo.api.Person;

public class NIOServiceImpl implements NIOService {

	public Person getPersion(String name) {
		Person r = new Person();
		r.setAge(123);
		r.setName(name);
		return r;
	}

}
