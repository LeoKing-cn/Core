package com.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import com.core.comm.entity.Log;

/** 
* 这里的@MapperScan就是上面所讲的Mapper扫描器中所需要的配置，会自动生成代理对象。 
* 注意，接口中的方法名称要和对应的MyBatis映射文件中的语句的id值一样，因为生成的 
* 动态代理，会根据这个匹配相应的Sql语句执行。另外就是方法的参数和返回值也需要注 
* 意。接口中的方法如何定义，对应的MyBatis映射文件就应该进行相应的定义。 
*/ 
@MapperScan
public interface LogAspectDao {
	
	Log selectByid(String id);
	
	Integer insertLog(Log log);

}
