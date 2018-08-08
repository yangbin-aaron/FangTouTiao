package com.aaron.fangtoutiao.uis.activitys;


import android.content.Intent;

import com.aaron.fangtoutiao.R;
import com.aaron.toolsaaron.viewtools.statusbar.Eyes;
import com.aaron.toolsaaron.ui.base.BaseActivity;
import com.aaron.toolsaaron.ui.base.BasePresenter;
import com.aaron.toolsaaron.utils.UIUtils;

/**
 * 欢迎界面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected int provideContentViewId() {
        return 0;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, UIUtils.getColor(R.color.white));
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
