package com.core.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.comm.entity.CoreUser;
import com.core.dao.LoginDao;
import com.core.service.LoginService;

@Service("Login")
public class LoginServiceImp implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Override
	public void login(CoreUser user) {
		loginDao.insertUser(user);
	}

}
