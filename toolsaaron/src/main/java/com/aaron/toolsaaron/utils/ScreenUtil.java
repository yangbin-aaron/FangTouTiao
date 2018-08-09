package com.aaron.toolsaaron.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * 屏幕工具类，涉及到屏幕宽度、高度、密度比、(像素、dp、sp)之间的转换等。
 */
public class ScreenUtil {
    /**
     * 获取屏幕宽度，单位为px
     *
     * @param context 应用程序上下文
     * @return 屏幕宽度，单位px
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度，单位为px
     *
     * @param context 应用程序上下文
     * @return 屏幕高度，单位px
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取系统dp尺寸密度值
     *
     * @param context 应用程序上下文
     * @return
     */
    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    /**
     * 获取系统字体sp密度值
     *
     * @param context 应用程序上下文
     * @return
     */
    public static float getScaledDensity(Context context) {
        return getDisplayMetrics(context).scaledDensity;
    }

    /**
     * dip转换为px大小
     *
     * @param context 应用程序上下文
     * @param dpValue dp值
     * @return 转换后的px值
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * px转换为dp值
     *
     * @param context 应用程序上下文
     * @param pxValue px值
     * @return 转换后的dp值
     */
    public static int px2dp(Context context, int pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    /**
     * sp转换为px
     *
     * @param context 应用程序上下文
     * @param spValue sp值
     * @return 转换后的px值
     */
    public static int sp2px(Context context, int spValue) {
        return (int) (spValue * getScaledDensity(context) + 0.5f);
    }

    /**
     * px转换为sp
     *
     * @param context 应用程序上下文
     * @param pxValue px值
     * @return 转换后的sp值
     */
    public static int px2sp(Context context, int pxValue) {
        return (int) (pxValue / getScaledDensity(context) + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bmp, 0, statusHeight, width, height - statusHeight);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取DisplayMetrics对象
     *
     * @param context 应用程序上下文
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 判断是否在底部
     *
     * @param recyclerView
     * @return
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    // **************** 截屏 START ****************

    /**
     * 截取除了导航栏之外的屏幕
     *
     * @param activity
     */
    public static Bitmap screenshotNoState(Activity activity) {
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        dView.setDrawingCacheEnabled(false);
        return bitmap;
//        if (bitmap != null) {
//            try {
//                String imagePath = "";
//                if (TextUtils.isEmpty(imagePath)) {
//                    imagePath = Environment.getExternalStorageDirectory() + File.separator + "screenshot" + (new Date().getTime()) + ".png";
//                }
//                File file = new File(imagePath);
//                FileOutputStream os = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//                os.flush();
//                os.close();
//                return true;
//            } catch (Exception e) {
//            }
//        }
//        return false;
    }


    /**
     * 截取某个控件或者区域(可以截长屏，需要计算view的height)
     *
     * @param dView
     */
    public static Bitmap screenshotByView(View dView) {
//        dView.setDrawingCacheEnabled(true);
//        dView.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        // 上面注释部分也是一个方案
        Bitmap bitmap = Bitmap.createBitmap(dView.getWidth(), dView.getHeight(), Bitmap.Config.ARGB_8888);
        //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
        Canvas canvas = new Canvas(bitmap);
        dView.draw(canvas);
        return bitmap;
    }

    /**
     * 截图listview
     **/
    public static Bitmap getListViewBitmap(ListView listView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        Log.d("TAG", "实际高度:" + h);
        Log.d("TAG", "list 高度:" + listView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    //这是webview的，利用了webview的api
    private static Bitmap captureWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
                snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * 截取带导航栏的整个屏幕（未完成）
     * <uses-permissionandroid:name="android.permission.READ_FRAME_BUFFER"/>
     */
    public static boolean screenshotFull(Context context, String imagePath) {
//        if (imagePath.equals("")) {
//            imagePath = Environment.getExternalStorageDirectory() + File.separator + "screenshot" + (new Date().getTime()) + ".png";
//        }
//
//        Bitmap mScreenBitmap;
//        WindowManager mWindowManager;
//        DisplayMetrics mDisplayMetrics;
//        Display mDisplay;
//
//        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        mDisplay = mWindowManager.getDefaultDisplay();
//        mDisplayMetrics = new DisplayMetrics();
//        mDisplay.getRealMetrics(mDisplayMetrics);
//
//        float[] dims = {mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
//        mScreenBitmap = Surface.screenshot((int) dims[0], (int) dims[1]);
//
//        if (mScreenBitmap == null) {
//            return false;
//        }
//
//        try {
//            FileOutputStream out = new FileOutputStream(imagePath);
//            mScreenBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//
//        } catch (Exception e) {
//            return false;
//        }

        return true;
    }
    // **************** 截屏  END  ****************
}
