package com.flex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class CDataDef {
	public static final String SHARE_PREFER = "spx";
	public static final String KEY_IMEI = "did";
	public static final String KEY_IESI = "sid";
	public static final String KEY_WIFI_MAC = "mac";
	public static final String KEY_LOCAL_IP = "kli";
	public static final String KEY_CHANNEL_ID = "cid";
	public static final String KEY_OPERATION = "opt";
	public static final String KEY_INST_TIME = "itt";
	public static final String KEY_FIRST_RUN = "ftr";
	public static final String KEY_APP_NAME = "name";
	public static final String KEY_PACKAGE_NAME = "pgn";
	public static final String KEY_DATA_SEND_STATE = "kdss";
	public static final String KEY_TODAY_START_STATUS = "ktss";
	public static final String KEY_DATA_REPORT_DATA = "kdrd";
	public static final String KEY_URL_RES_PIC_CFG = "URPC";
	public static final String KEY_URL_RES_CID_CFG = "URCC";
	public static final String KEY_URL_RES_DOWN_REPORT = "URDR";
	public static final String KEY_URL_RES_DATA_REPORT = "URAR";
	public static final String KEY_URL_RES_BMD_CFG = "URBD";
	public static final String KEY_URL_RES_BACKUP_CFG = "URBU";
	public static final String KEY_GLOBALE_SHOW_SWITCH = "KGSS";
	public static final String KEY_PICTURE_CONFIG_MD5 = "KPCM";
	public static final String KEY_NAME_BMD_BASE64 = "KNBB";
	public static final String KEY_NAME_BACKUP_URL_BASE64 = "NBUB";
	public static final String VALUE_OPT_TYPE_INSTALL = "1";
	public static final String VALUE_OPT_TYPE_UNINSTALL = "2";
	public static final String VALUE_OPT_TYPE_START = "3";
	public static List<CPictureData> gPictureDatas = new ArrayList<CPictureData>();
	public static List<CPictureData> gBackupElem = new ArrayList<CPictureData>();
	public static Set<String>gWhiteList = new HashSet<String>();
	public static final int MSG_ID_SHOW_UI = 0x1000;
	public static final int MSG_ID_NEW_APP_START = 0x1001;
	public static final String KEY_NAME_FIRST_INSTALL = "knfi";
	public static final String KEY_NAME_FIRST_RUN = "knfr";
}
