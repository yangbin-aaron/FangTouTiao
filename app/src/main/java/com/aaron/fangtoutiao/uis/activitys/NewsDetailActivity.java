package com.aaron.fangtoutiao.uis.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.NewsDetail;
import com.aaron.fangtoutiao.uis.view.NewsDetailHeaderView;
import com.aaron.toolsaaron.utils.GlideUtils;
import com.aaron.toolsaaron.utils.LogUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class NewsDetailActivity extends NewsDetailBaseActivity {
    @Bind(R.id.iv_back)
    ImageView mIvBack;

    @Bind(R.id.ll_user)
    LinearLayout mLlUser;

    @Bind(R.id.iv_avatar)
    ImageView mIvAvatar;

    @Bind(R.id.tv_author)
    TextView mTvAuthor;


    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initListener() {
        super.initListener();

        final int llInfoBottom = mHeaderView.mLlInfo.getBottom();
        final LinearLayoutManager layoutManager = (LinearLayoutManager) mRvComment.getLayoutManager();
        mRvComment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisiableChildView.getHeight();
                int scrollHeight = (position) * itemHeight - firstVisiableChildView.getTop();

                LogUtil.i("scrollHeight: " + scrollHeight);
                LogUtil.i("llInfoBottom: " + llInfoBottom);

                mLlUser.setVisibility(scrollHeight > llInfoBottom ? View.VISIBLE : View.GONE);//如果滚动超过用户信息一栏，显示标题栏中的用户头像和昵称
            }
        });
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        mHeaderView.setDetail(newsDetail, new NewsDetailHeaderView.LoadWebListener() {
            @Override
            public void onLoadFinished() {
                //加载完成后，显示内容布局
                mStateView.showContent();
            }
        });

        mLlUser.setVisibility(View.GONE);

        if (newsDetail.media_user != null){
            GlideUtils.loadRound(this,newsDetail.media_user.avatar_url, mIvAvatar);
            mTvAuthor.setText(newsDetail.media_user.screen_name);
        }
    }

    @Override
    public void onBackPressed() {
        postVideoEvent(false);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        postVideoEvent(false);
    }
}
