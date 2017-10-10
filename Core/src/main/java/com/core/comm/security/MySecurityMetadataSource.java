package com.core.comm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;


/**
 * 加载资源与权限的对应关系
 * @author admin
 * @version 1.0v
 * */
@Service
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	//Url资源
	private static Map<String, Collection<ConfigAttribute>> urlResourceMap = null;
	//页面元素资源
	private static Map<String, Collection<ConfigAttribute>> pageResourceMap = null;

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}
	/**
	 * @PostConstruct是Java EE 5引入的注解，
	 * Spring允许开发者在受管Bean中使用它。当DI容器实例化当前受管Bean时，
	 * @PostConstruct注解的方法会被自动触发，从而完成一些初始化工作，
	 * 
	 * //加载所有资源与权限的关系
	 */
	@PostConstruct
	private void loadResourceDefine() {
	}
	//返回所请求url资源所需要的权限
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//		System.err.println("-----------MySecurityMetadataSource getAttributes ----------- ");
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
	//	System.out.println("requestUrl is " + requestUrl);
		if(urlResourceMap == null) {
			loadResourceDefine();
		}
		//System.err.println("resourceMap.get(requestUrl); "+resourceMap.get(requestUrl));
		if(requestUrl.indexOf("?")>-1){
			requestUrl=requestUrl.substring(0,requestUrl.indexOf("?"));
		}
		Collection<ConfigAttribute> configAttributes = getConfigAttributes(requestUrl);
		if(configAttributes == null){
			Collection<ConfigAttribute> returnCollection = new ArrayList<ConfigAttribute>();
			returnCollection.add(new SecurityConfig("NO_RIGHTS")); 
			
			return returnCollection;
		}
		return configAttributes;
	}


	public Map<String, Collection<ConfigAttribute>> getPageResourceMap() {
		return pageResourceMap;
	}

	private Collection<ConfigAttribute> getConfigAttributes(String requestUrl){
		
		Collection<ConfigAttribute> configAttributes = null;

		configAttributes = urlResourceMap.get(requestUrl);
		
		if(configAttributes!=null){
			return configAttributes;
		}
		
		
		Set<String> keys = urlResourceMap.keySet();
		AntPathMatcher matcher = new AntPathMatcher();
		

		for(String key:keys){
			if(matcher.match(key, requestUrl)){
				configAttributes=urlResourceMap.get(key);
				break;
			}
		}
		return configAttributes;
		
	}
}