package com.aaron.fangtoutiao.uis.activitys;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.constants.Constant;
import com.aaron.fangtoutiao.model.entity.CommentData;
import com.aaron.fangtoutiao.model.entity.NewsDetail;
import com.aaron.fangtoutiao.model.event.DetailCloseEvent;
import com.aaron.fangtoutiao.model.response.CommentResponse;
import com.aaron.fangtoutiao.mvp.presenter.NewsDetailPresenter;
import com.aaron.fangtoutiao.mvp.view.INewsDetailView;
import com.aaron.fangtoutiao.uis.adapter.CommentAdapter;
import com.aaron.fangtoutiao.uis.view.NewsDetailHeaderView;
import com.aaron.jcvideoplayer_lib.JCMediaManager;
import com.aaron.jcvideoplayer_lib.JCVideoPlayerManager;
import com.aaron.toolsaaron.recyclerview.BaseQuickAdapter;
import com.aaron.toolsaaron.recyclerview.powerfulrecyclerview.PowerfulRecyclerView;
import com.aaron.toolsaaron.ui.base.BaseActivity;
import com.aaron.toolsaaron.utils.ListUtils;
import com.aaron.toolsaaron.utils.UIUtils;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public abstract class NewsDetailBaseActivity extends BaseActivity<NewsDetailPresenter> implements INewsDetailView, BaseQuickAdapter.RequestLoadMoreListener {
    public static final String CHANNEL_CODE = "channelCode";
    public static final String PROGRESS = "progress";
    public static final String POSITION = "position";
    public static final String DETAIL_URL = "detailUrl";
    public static final String GROUP_ID = "groupId";
    public static final String ITEM_ID = "itemId";

    @Bind(R.id.fl_content)
    FrameLayout mFlContent;

    @Bind(R.id.rv_comment)
    PowerfulRecyclerView mRvComment;

    @Bind(R.id.tv_comment_count)
    TextView mTvCommentCount;

    private List<CommentData> mCommentList = new ArrayList<>();
    protected StateView mStateView;
    private CommentAdapter mCommentAdapter;
    protected NewsDetailHeaderView mHeaderView;
    private String mDetalUrl;
    private String mGroupId;
    private String mItemId;
    protected CommentResponse mCommentResponse;

    protected String mChannelCode;
    protected int mPosition;

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return getViewContentViewId();
    }

    protected abstract int getViewContentViewId();

    @Override
    public void initView() {
        mStateView = StateView.inject(mFlContent);
        mStateView.setLoadingResource(R.layout.page_loading);
        mStateView.setRetryResource(R.layout.page_net_error);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        mChannelCode = intent.getStringExtra(CHANNEL_CODE);
        mPosition = intent.getIntExtra(POSITION, 0);

        mDetalUrl = intent.getStringExtra(DETAIL_URL);
        mGroupId = intent.getStringExtra(GROUP_ID);
        mItemId = intent.getStringExtra(ITEM_ID);
        if (TextUtils.isEmpty(mItemId)) mItemId = "null";
        mItemId = mItemId.replace("i", "");

        mPresenter.getNewsDetail(mDetalUrl);
        loadCommentData();
    }

    private void loadCommentData() {
        mStateView.showLoading();
        mPresenter.getComment(mGroupId, mItemId, 1);
    }

    @Override
    public void initListener() {
        mCommentAdapter = new CommentAdapter(this, R.layout.item_comment, mCommentList);
        mRvComment.setAdapter(mCommentAdapter);

        mHeaderView = new NewsDetailHeaderView(this);
        mCommentAdapter.addHeaderView(mHeaderView);

        mCommentAdapter.setEnableLoadMore(true);
        mCommentAdapter.setOnLoadMoreListener(this, mRvComment);

        mCommentAdapter.setEmptyView(R.layout.pager_no_comment);
        mCommentAdapter.setHeaderAndEmpty(true);

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                loadCommentData();
            }
        });
    }


    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {

    }

    @Override
    public void onGetCommentSuccess(CommentResponse response) {

        mCommentResponse = response;

        if (ListUtils.isEmpty(response.data)) {
            //没有评论了
            mCommentAdapter.loadMoreEnd();
            return;
        }

        if (response.total_number > 0) {
            //如果评论数大于0，显示红点
            mTvCommentCount.setVisibility(View.VISIBLE);
            mTvCommentCount.setText(String.valueOf(response.total_number));
        }

        mCommentList.addAll(response.data);
        mCommentAdapter.notifyDataSetChanged();

        if (!response.has_more) {
            mCommentAdapter.loadMoreEnd();
        } else {
            mCommentAdapter.loadMoreComplete();
        }
    }


    @Override
    public void onError() {
        mStateView.showRetry();
    }


    @OnClick({R.id.fl_comment_icon, R.id.tv_edit_comment, R.id.iv_save, R.id.iv_shape})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_comment_icon:
                //底部评论的图标
                RecyclerView.LayoutManager layoutManager = mRvComment.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int firstPosition = linearManager.findFirstVisibleItemPosition();
                    int last = linearManager.findLastVisibleItemPosition();
                    if (firstPosition == 0 && last == 0) {
                        //处于头部，滚动到第一个条目
                        mRvComment.scrollToPosition(1);
                    } else {
                        //不是头部，滚动到头部
                        mRvComment.scrollToPosition(0);
                    }
                }
                break;
            case R.id.tv_edit_comment:
                UIUtils.showToast("评论，敬请期待！");
                break;
            case R.id.iv_save:
                UIUtils.showToast("收藏，敬请期待！");
                break;
            case R.id.iv_shape:
                UIUtils.showToast("转发，敬请期待！");
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getComment(mGroupId, mItemId, mCommentList.size() / Constant.COMMENT_PAGE_SIZE + 1);
    }


    /**
     * 发送事件，用于更新上个页面的播放进度以及评论数
     */
    protected void postVideoEvent(boolean isVideoDetail) {
        DetailCloseEvent event = new DetailCloseEvent();
        event.setChannelCode(mChannelCode);
        event.setPosition(mPosition);

        if (mCommentResponse != null) {
            event.setCommentCount(mCommentResponse.total_number);
        }

        if (isVideoDetail && JCMediaManager.instance().mediaPlayer != null && JCVideoPlayerManager.getCurrentJcvd() != null) {
            //如果是视频详情
            int progress = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
            event.setProgress(progress);
        }

        EventBus.getDefault().postSticky(event);
        finish();
    }
}
