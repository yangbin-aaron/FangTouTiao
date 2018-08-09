package com.aaron.fangtoutiao.uis.activitys;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.ImageView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.NewsDetail;
import com.aaron.fangtoutiao.uis.view.NewsDetailHeaderView;
import com.aaron.fangtoutiao.utils.VideoPathDecoder;
import com.aaron.jcvideoplayer_lib.JCVideoPlayer;
import com.aaron.jcvideoplayer_lib.JCVideoPlayerStandard;
import com.aaron.toolsaaron.utils.LogUtil;
import com.aaron.toolsaaron.utils.UIUtils;
import com.aaron.toolsaaron.viewtools.statusbar.Eyes;

import butterknife.Bind;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @description: 视频新闻
 */

public class VideoDetailActivity extends NewsDetailBaseActivity {

    @Bind(R.id.video_player)
    JCVideoPlayerStandard mVideoPlayer;

    private SensorManager mSensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    private int mProgress;
    private int mPosition;
    private String mChannelCode;

    @Override
    public void initView() {
        super.initView();
        Eyes.setStatusBarColor(this, UIUtils.getColor(android.R.color.black));
    }

    @Override
    public void initData() {
        super.initData();
        mProgress = getIntent().getIntExtra(PROGRESS, 0);
    }

    @Override
    public void initListener() {
        super.initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        mVideoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        mVideoPlayer.titleTextView.setVisibility(GONE);
    }

    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onGetNewsDetailSuccess(final NewsDetail newsDetail) {
        newsDetail.content = "";
        mHeaderView.setDetail(newsDetail, new NewsDetailHeaderView.LoadWebListener() {
            @Override
            public void onLoadFinished() {
                //加载完成后，显示内容布局
                mStateView.showContent();
            }
        });

        VideoPathDecoder decoder = new VideoPathDecoder() {
            @Override
            public void onSuccess(final String url) {
                LogUtil.e("onGetNewsDetailSuccess", url);
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        mVideoPlayer.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST, newsDetail.title);
                        mVideoPlayer.seekToInAdvance = mProgress;//设置进度
                        mVideoPlayer.startVideo();
                    }
                });
            }

            @Override
            public void onDecodeError() {

            }
        };
        decoder.decodePath(newsDetail.url);
        LogUtil.e("onGetNewsDetailSuccess", newsDetail.url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        postVideoEvent(true);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        postVideoEvent(true);
    }
}
