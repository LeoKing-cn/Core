package com.core.comm.utils;

import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) {
		aa();
	}
	public static void aa(){
		/*String shop_type ="";
		String fee_type = "1";
		if(("1".equals(shop_type))||(!"1".equals(shop_type))&&"1".equals(fee_type)){
			System.out.println("aaa");
		}*/
		
		/*for(int i=0;i<10;i++){
			getResultMap();
		}*/
		String a = "你好";
		String b = "你好";
		System.out.println(a==b);
	}
	
	public static Map<String,Object> resultMap = null;
	public static Map<String,Object> getResultMap(){
		if(resultMap==null){
			resultMap = new HashMap<String,Object>();
		}
		return resultMap;
	}

}
