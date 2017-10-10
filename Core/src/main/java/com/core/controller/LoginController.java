package com.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.comm.entity.CoreUser;
import com.core.service.LoginService;
@Controller
@RequestMapping("/index")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	
	@RequestMapping("/index")
	public void sendSms() {
		System.out.println("ssssssssssssssssssssss");
		CoreUser user = new CoreUser();
		user.setName("小明");
		user.setAdress("aaa");
		loginService.login(user);
	}

}
