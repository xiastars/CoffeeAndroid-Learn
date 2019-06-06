package com.summer.helper.utils;
/**
 * 正则表达式相关
 * @author xiaqiliang
 *
 */
public class PatternUtils {
	
	public static void main(String[] args) {
		boolean result = stringStartWith("-78989","-");
		System.out.print(result);
		
	}
	public static boolean stringStartWith(String content,String prefix ){
		return content.matches(prefix);
	}

}
