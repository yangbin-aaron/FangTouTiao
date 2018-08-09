package com.aaron.fangtoutiao.uis.adapter.news;

import android.widget.ImageView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.News;
import com.aaron.fangtoutiao.uis.adapter.NewsListAdapter;
import com.aaron.toolsaaron.recyclerview.BaseViewHolder;
import com.aaron.toolsaaron.utils.GlideUtils;

/**
 * @description: 三张图片布局(文章、广告)
 */
public class ThreePicNewsItemProvider extends BaseNewsItemProvider {

    public ThreePicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return NewsListAdapter.THREE_PICS_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_three_pics_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
        //三张图片的新闻
        GlideUtils.load(mContext, news.image_list.get(0).url, (ImageView) helper.getView(R.id.iv_img1));
        GlideUtils.load(mContext, news.image_list.get(1).url, (ImageView) helper.getView(R.id.iv_img2));
        GlideUtils.load(mContext, news.image_list.get(2).url, (ImageView) helper.getView(R.id.iv_img3));
    }

}
