package com.core.comm.aop;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.core.comm.entity.Log;
import com.core.dao.LogAspectDao;


public class LogDaoAspect {
	private Integer paras;

	private Integer results;

	private Integer openAspect;

	public Integer getParas() {
		return paras;
	}

	public void setParas(Integer paras) {
		this.paras = paras;
	}

	public Integer getResults() {
		return results;
	}

	public void setResults(Integer results) {
		this.results = results;
	}

	public Integer getOpenAspect() {
		return openAspect;
	}

	public void setOpenAspect(Integer openAspect) {
		this.openAspect = openAspect;
	}

	@Autowired
	private LogAspectDao logAspectDao;

	public void beforeMethod(JoinPoint joinpoint) {
		// id level method_name para result create_time create_by update_time

	}

	public void afterMethod(JoinPoint joinpoint) {

	}

	public void afterReturning(JoinPoint joinPoint, Object result) {

	}

	public void afterThrowing(JoinPoint joinPoint, Exception ex) {

	}

	public Object around(ProceedingJoinPoint joinpoint) throws Throwable {
		Object result = null;
		if (openAspect == 0) {// 控制是否启用改aop
			result = joinpoint.proceed();
			return result;
		}
		// method_name
		String methodName = joinpoint.getSignature().getName();// 获取方法名
		Class<?> targetClass = joinpoint.getTarget().getClass();// 获取目标对象的类名
		Method method = null;
		for (Method mt : targetClass.getMethods()) {
			if (methodName.equals(mt.getName())) {
				method = mt;
				break;
			}
		}
		// level
		String level = "";
		level = "Dao";

		// para
		String[] names = ((CodeSignature) joinpoint.getSignature()).getParameterNames();
		Object[] args = joinpoint.getArgs();
		StringBuilder sb = new StringBuilder("para: ");
		if (names != null && names.length > 0) {
			for (int i = 0; i < names.length; i++) {
				String typeName = "";
				if (args[i] != null && args[i].getClass().getName() != null) {
					typeName = args[i].getClass().getName();
				}
				String jsonString = JSON.toJSONString(args[i]);
				sb.append(names[i] + "(type:" + typeName + ") = " + jsonString + ", ");
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				String typeName = "";
				if (args[i] != null && args[i].getClass().getName() != null) {
					typeName = args[i].getClass().getName();
				}
				String jsonString = JSON.toJSONString(args[i]);
				sb.append("para" + i + "(type:" + typeName + ") =" + jsonString + ", ");
			}
		}

		// result
		String resultString = "";
		// 不知道为什么 dao 层 不能获取 resultString ,会报错，报主键重复.
		// resultString = joinpoint.proceed().toString();

		// create_name
		// SecurityContext securityContext =
		// SecurityContextHolder.getContext();
		// Authentication authentication =
		// securityContext.getAuthentication();
		// String create_name = authentication.getName();

		// start_time
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Log log = new Log();
		log.setLevel(level);
		log.setMethodName(joinpoint.getTarget().getClass().getName() + "." + method.getName());
		if (paras == 1) {
			log.setPara(sb.toString());
		}
		log.setStartTime(dateformat.parse(dateformat.format(System.currentTimeMillis())));
		result = joinpoint.proceed();
		resultString = JSON.toJSONString(result);
		Signature signature = joinpoint.getSignature();
		Class returnType = ((MethodSignature) signature).getReturnType();
		if (results == 1) {
			log.setResult("return : (type:" + returnType + ") =" + resultString);
		}
		log.setEndTime(dateformat.parse(dateformat.format(System.currentTimeMillis())));
		try {
			logAspectDao.insertLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
