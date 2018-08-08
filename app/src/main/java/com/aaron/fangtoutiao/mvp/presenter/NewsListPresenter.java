package com.aaron.fangtoutiao.mvp.presenter;

import com.aaron.fangtoutiao.model.entity.News;
import com.aaron.fangtoutiao.mvp.view.lNewsListView;
import com.aaron.toolsaaron.ui.base.BasePresenter;
import com.aaron.toolsaaron.utils.ListUtils;
import com.aaron.toolsaaron.utils.LogUtil;
import com.aaron.toolsaaron.utils.PreUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewsListPresenter extends BasePresenter<lNewsListView> {
    private long lastTime;

    public NewsListPresenter(lNewsListView view) {
        super(view);
    }

    public void getNewsList(String channelCode) {
        lastTime = PreUtils.getLong(channelCode, 0);//读取对应频道下最后一次刷新的时间戳
        if (lastTime == 0) {
            //如果为空，则是从来没有刷新过，使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }

//        addSubscription(mApiService.getNewsList(channelCode, lastTime, System.currentTimeMillis() / 1000), new Subscriber<NewsResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                LogUtil.e(e.getLocalizedMessage());
//                mView.onError();
//            }
//
//            @Override
//            public void onNext(NewsResponse response) {
//                lastTime = System.currentTimeMillis() / 1000;
//                PreUtils.putLong(channelCode, lastTime);//保存刷新的时间戳
//
//                List<NewsData> data = response.data;
//                List<News> newsList = new ArrayList<>();
//                if (!ListUtils.isEmpty(data)) {
//                    for (NewsData newsData : data) {
//                        News news = new Gson().fromJson(newsData.content, News.class);
//                        newsList.add(news);
//                    }
//                }
//                LogUtil.e(newsList);
//                mView.onGetNewsListSuccess(newsList, response.tips.display_info);
//            }
//        });
    }
}
