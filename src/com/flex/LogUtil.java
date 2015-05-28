package com.flex;
import android.util.Log;
public class LogUtil {
	public static LogUtil mInstance;
	public static boolean IS_DEBUG = false;
	static{
		mInstance = null;
	}
	public static void Log(String tag ,String msg){
		if(IS_DEBUG){
			Log.i(tag, msg);
		}
	}
}
