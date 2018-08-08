package com.aaron.fangtoutiao.mvp.view;

import com.aaron.fangtoutiao.model.entity.NewsDetail;
import com.aaron.fangtoutiao.model.response.CommentResponse;

/**
 * @author ChayChan
 * @description: 新闻详情的回调
 * @date 2017/6/28  15:41
 */

public interface INewsDetailView {

    void onGetNewsDetailSuccess(NewsDetail newsDetail);

    void onGetCommentSuccess(CommentResponse response);

    void onError();
}
