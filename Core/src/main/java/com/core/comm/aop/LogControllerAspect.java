package com.core.comm.aop;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.core.comm.entity.Log;
import com.core.dao.LogAspectDao;

public class LogControllerAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
		Log log = new Log();
		// start_time
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// result
		String resultString = "";
		try {
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
			String level = "Controller";
			log.setLevel(level);

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

				log.setMethodName(joinpoint.getTarget().getClass().getName() + "." + method.getName());
			if (paras == 1) {
				log.setPara(sb.toString());
			}
			log.setStartTime(dateformat.parse(dateformat.format(System.currentTimeMillis())));
		} catch (Exception e) {
			log.setMethodName(" ");
			logger.error(e.getMessage(), e);
		}

		result = joinpoint.proceed();

		try {
			resultString = JSON.toJSONString(result);
			Signature signature = joinpoint.getSignature();
			Class returnType = ((MethodSignature) signature).getReturnType();
			if (results == 1) {
				log.setResult("return : (type:" + returnType + ") =" + resultString);
			}
			log.setEndTime(dateformat.parse(dateformat.format(System.currentTimeMillis())));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			logAspectDao.insertLog(log);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}

}
