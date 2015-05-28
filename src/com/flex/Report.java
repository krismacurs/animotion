package com.flex;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("SimpleDateFormat")
public class Report extends Thread {
	private Context mContext;
	private String mAppName;
	public Report(Context context,String appname){
		mContext = context;
		mAppName = appname;
	}
	public void run(){
		FuncMod fm = FuncMod.getCmInstance();
		String url = fm.getDatasFromCached(mContext, DataDef.KEY_URL_RES_DOWN_REPORT);
		String params = createReportParams(fm);
		fm.sendPost(url , params);
	}
	
	private String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String now = sdf.format(new Date());
		return now;
	}
	
	private String createReportParams(FuncMod fm){
		StringBuilder sb = new StringBuilder();
		String value_cid = fm.getDatasFromCached(mContext, DataDef.KEY_CHANNEL_ID);
		String value_did = fm.getDatasFromCached(mContext, DataDef.KEY_IMEI);
		String mac = fm.getDatasFromCached(mContext, DataDef.KEY_WIFI_MAC);
		String time = getCurrentTime();
		sb.append(DataDef.KEY_CHANNEL_ID).append("=").append(value_cid)
		.append("&").append(DataDef.KEY_IMEI).append("=").append(value_did)
		.append("&").append(DataDef.KEY_WIFI_MAC).append("=").append(mac)
		.append("&").append(DataDef.KEY_APP_NAME).append("=").append(mAppName)
		.append("&time=").append(time).append("&ver=1.1");
		return sb.toString();
	}
}
