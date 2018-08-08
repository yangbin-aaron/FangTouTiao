package com.aaron.toolsaaron.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * author：   zp
 * date：     2015/8/19 0019 17:47
 * description: 网络工具
 * modify by
 */
public class NetWorkUtil {

    public static final int INVALID_NETWORK_TYPE = -1;
    public static final int MOBILE_NETWORK_TYPE = 0;
    public static final int WIF_NETWORK_TYPE = 1;

    /**
     * 获取当前网络连接类型
     *
     * @param context context
     * @return -1不可用，0移动网络，WIFI网络
     */
    public static int getNetWorkType(Context context) {
        NetworkInfo mNetworkInfo = null;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        }
        return (mNetworkInfo != null && mNetworkInfo.isAvailable()) ? mNetworkInfo.getType() : INVALID_NETWORK_TYPE;
    }

    /**
     * 当前是否为WIFI连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 当前是否为移动网络
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }


    /**
     * 获取apn 类型
     *
     * @param context context
     * @return 类型
     */
    public static String getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return null;
        }
        if (networkInfo.getExtraInfo() == null) {
            return null;
        }
        return networkInfo.getExtraInfo().toLowerCase();
    }


    /**
     * 判断当前是否有网络连接
     * @param context
     * @return	有网络返回true；无网络返回false
     */
    @SuppressWarnings("null")
    public static boolean isNetworkAvailable(Context context){
        if(context !=null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info !=null){
                return info.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;
        isMobileDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }

    /**
     * 判断wifi 是否可用
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isWifiDataEnable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = manager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }

    /**
     * 跳转到网络设置页面
     * @param activity
     */
    public static void GoSetting(Activity activity){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cn);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
