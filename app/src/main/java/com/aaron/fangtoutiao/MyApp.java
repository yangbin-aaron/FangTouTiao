package com.aaron.fangtoutiao;

import com.aaron.toolsaaron.BaseApplication;
import com.aaron.toolsaaron.utils.PreUtils;

public class MyApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PreUtils.init("FangTouTiao" + "-v1");
    }
}
