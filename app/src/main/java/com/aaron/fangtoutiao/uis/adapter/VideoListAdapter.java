package com.aaron.fangtoutiao.uis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.News;
import com.aaron.fangtoutiao.utils.VideoPathDecoder;
import com.aaron.jcvideoplayer_lib.JCVideoPlayer;
import com.aaron.jcvideoplayer_lib.JCVideoPlayerStandard;
import com.aaron.jcvideoplayer_lib.OnVideoClickListener;
import com.aaron.toolsaaron.recyclerview.BaseQuickAdapter;
import com.aaron.toolsaaron.recyclerview.BaseViewHolder;
import com.aaron.toolsaaron.utils.GlideUtils;
import com.aaron.toolsaaron.utils.LogUtil;
import com.aaron.toolsaaron.utils.TimeUtils;
import com.aaron.toolsaaron.utils.UIUtils;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideoListAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    public VideoListAdapter(@Nullable List<News> data) {
        super(R.layout.item_video_list, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final News news) {
        if (TextUtils.isEmpty(news.title)) {
            //如果没有标题，则直接跳过
            return;
        }

        helper.setVisible(R.id.ll_title, true);//显示标题栏
        helper.setText(R.id.tv_title, news.title);//设置标题

        String format = UIUtils.getString(R.string.video_play_count);
        int watchCount = news.video_detail_info.video_watch_count;
        String countUnit = "";
        if (watchCount > 10000) {
            watchCount = watchCount / 10000;
            countUnit = "万";
        }

        helper.setText(R.id.tv_watch_count, String.format(format, watchCount + countUnit));//播放次数
        GlideUtils.loadRound(mContext, news.user_info.avatar_url, (ImageView) helper.getView(R.id.iv_avatar));//作者头像

        helper.setVisible(R.id.ll_duration, true)//显示时长
                .setText(R.id.tv_duration, TimeUtils.secToTime(news.video_duration));//设置时长

        helper.setText(R.id.tv_author, news.user_info.name)//昵称
                .setText(R.id.tv_comment_count, String.valueOf(news.comment_count));//评论数

        final JCVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);
        //设置缩略图
        GlideUtils.load(mContext, news.video_detail_info.detail_video_large_image.url, videoPlayer.thumbImageView, R.color.color_d8d8d8);

        videoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        videoPlayer.tinyBackImageView.setVisibility(GONE);
        videoPlayer.setPosition(helper.getAdapterPosition());//绑定Position

        videoPlayer.titleTextView.setText("");//清除标题,防止复用的时候出现

        videoPlayer.setOnVideoClickListener(new OnVideoClickListener() {
            @Override
            public void onVideoClickToStart() {
                //点击播放
                helper.setVisible(R.id.ll_duration, false);//隐藏时长
                helper.setVisible(R.id.ll_title,false);//隐藏标题栏
                //
                VideoPathDecoder decoder = new VideoPathDecoder() {
                    @Override
                    public void onSuccess(final String url) {
                        LogUtil.e("Video url:" + url);
                        UIUtils.postTaskSafely(new Runnable() {
                            @Override
                            public void run() {
                                videoPlayer.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST,news.title);
                                videoPlayer.seekToInAdvance = news.video_detail_info.progress;
                                videoPlayer.startVideo();
                            }
                        });
                    }

                    @Override
                    public void onDecodeError() {
                    }
                };
                decoder.decodePath(news.url);
            }
        });
    }
}
