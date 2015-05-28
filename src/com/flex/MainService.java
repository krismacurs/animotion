package com.flex;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MainService extends Service {
	public static MainService mInstance = null;
	public static Context mContext = null;
	public static String tag = MainService.class.getName();
	public LogUtil mInstanceLogger;
	public static String serviceName;
	private static boolean reged = false;
	private static Handler mSendMsg;
	private final static long delayMillis = 1000*60*60*5;
	public static Runnable ThreadRunnable;
	public static MainService getInstance() {
		if (mInstance == null)
			mInstance = new MainService();
		return mInstance;
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		if (mContext == null) {
			mContext = getApplicationContext();
		}
		UI ui = UI.getUiInstance(mContext);
		if(UI.recMsg == null){
			ui.setMessage();
		}
		if(reged == false){
			regAppDownCompleteReceiver(mContext);
			reged = true;
		}
		
		if(mSendMsg == null){
			mSendMsg = new Handler();
		}
		MainThread fmt = MainThread.getFlexThreadInstance(mContext);
		fmt.start();
		StartHeartBeat();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void regAppDownCompleteReceiver(Context context){
		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		context.registerReceiver(new DownloadReceiver(context), filter);
	}
	
	
	public void StartHeartBeat(){
		ThreadRunnable = new Runnable(){
			public void run() {
				if(UI.isShowing == false){
					LogUtil.Log(tag, "not showing,send message");
					FuncMod fm = FuncMod.getCmInstance();
					boolean safeOrNot = fm.isCurrentActivityInBmd(DataDef.gWhiteList, mContext);
					Bundle bd = new Bundle();
					bd.putBoolean("safe", safeOrNot);
					Message msg = Message.obtain();
					msg.what = DataDef.MSG_ID_SHOW_UI;
					msg.obj = DataDef.gPictureDatas;
					msg.setData(bd);
					UI.recMsg.sendMessage(msg);
					mSendMsg.postDelayed(ThreadRunnable, delayMillis);
				}else{
					LogUtil.Log(tag, "showing.");
				}
			}	
		};
		mSendMsg.postDelayed(ThreadRunnable, delayMillis);
	}
}
