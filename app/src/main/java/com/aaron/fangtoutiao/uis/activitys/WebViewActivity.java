package com.aaron.fangtoutiao.uis.activitys;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aaron.fangtoutiao.R;
import com.aaron.toolsaaron.ui.base.BaseActivity;
import com.aaron.toolsaaron.ui.base.BasePresenter;

import butterknife.Bind;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {
    public static final String URL = "url";

    @Bind(R.id.iv_back)
    ImageView mIvBack;

    @Bind(R.id.tv_author)
    TextView mTvTitle;

    @Bind(R.id.pb_loading)
    ProgressBar mPbLoading;

    @Bind(R.id.wv_content)
    WebView mWvContent;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra(URL);
        mWvContent.loadUrl(url);
    }

    @Override
    public void initListener() {
        WebSettings settings = mWvContent.getSettings();
        settings.setJavaScriptEnabled(true);


        mWvContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mPbLoading.setVisibility(View.GONE);
            }
        });

        mWvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mPbLoading.setProgress(newProgress);
            }
        });

        mWvContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWvContent.canGoBack()) {  //表示按返回键
                        mWvContent.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        finish();
    }
}
