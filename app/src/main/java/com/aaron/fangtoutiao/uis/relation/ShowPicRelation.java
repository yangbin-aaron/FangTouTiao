package com.aaron.fangtoutiao.uis.relation;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.aaron.fangtoutiao.uis.activitys.ImageViewPagerActivity;
import com.aaron.toolsaaron.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ShowPicRelation {

    private static final String TAG = ShowPicRelation.class.getSimpleName();

    private Context mContext;
    private List<String> mUrls = new ArrayList();

    public ShowPicRelation(Context context) {
        this.mContext = context;
    }

    /**
     * JS中点击图片执行的Java代码
     */
    @JavascriptInterface
    public void openImg(String url) {
        //传到展示图片的viewPager
        Intent intent = new Intent(mContext, ImageViewPagerActivity.class);
        intent.putExtra(ImageViewPagerActivity.POSITION, mUrls.indexOf(url));
        intent.putStringArrayListExtra(ImageViewPagerActivity.IMG_URLS, (ArrayList<String>) mUrls);
        mContext.startActivity(intent);
    }

    /**
     * 页面加载时JS调用的Java代码
     */
    @JavascriptInterface
    public void getImgArray(String urlArray) {
        LogUtil.i(TAG, urlArray);
        String[] urls = urlArray.split(";");//url拼接成的字符串，有分号隔开
        for (String url : urls) {
            mUrls.add(url);
        }
    }
}
