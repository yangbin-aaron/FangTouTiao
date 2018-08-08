package com.aaron.fangtoutiao.uis.fragments;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.News;
import com.aaron.fangtoutiao.mvp.presenter.NewsListPresenter;
import com.aaron.fangtoutiao.mvp.view.lNewsListView;
import com.aaron.toolsaaron.ui.base.BaseFragment;

import java.util.List;

public class NewsListFragment extends BaseFragment<NewsListPresenter> implements lNewsListView {
    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onGetNewsListSuccess(List<News> newList, String tipInfo) {

    }

    @Override
    public void onError() {

    }
}
