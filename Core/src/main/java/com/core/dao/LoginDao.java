package com.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import com.core.comm.entity.CoreUser;

@MapperScan
public interface LoginDao {
	
	int insertUser(CoreUser record);

}
