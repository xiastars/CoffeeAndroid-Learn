package com.summer.demo.utils;

import android.content.Context;
import android.content.Intent;

/**
 * 这里定义了一个跳转的类，使用了单例模式
 * @编者 夏起亮
 *
 */
public class JumpTo {
	
	private static JumpTo jumpTo = null;

	public static synchronized JumpTo getInstance() {
		if (jumpTo == null)
			jumpTo = new JumpTo();
		return jumpTo;
	}
	
	/**
	 * 普通的跳转方法，不需要传递数据
	 * @param context
	 * @param cls
	 */
	public void commonJump(Context context, Class<?> cls){
		Intent intent = new Intent(context,cls);
		context.startActivity(intent);
	}	

}
