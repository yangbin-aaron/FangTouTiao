package com.aaron.fangtoutiao.uis.adapter.news;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.News;
import com.aaron.fangtoutiao.uis.adapter.NewsListAdapter;
import com.aaron.toolsaaron.recyclerview.BaseViewHolder;

/**
 * @description: 纯文本新闻
 */
public class TextNewsItemProvider extends BaseNewsItemProvider {

    public TextNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return NewsListAdapter.TEXT_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_text_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
         //由于文本消息的逻辑目前已经在基类中封装，所以此处无须写
        //定义此类是提供文本消息的ItemProvider
    }
}
