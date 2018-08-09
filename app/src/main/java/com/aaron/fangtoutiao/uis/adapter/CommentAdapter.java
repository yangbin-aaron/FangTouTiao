package com.aaron.fangtoutiao.uis.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.entity.CommentData;
import com.aaron.toolsaaron.recyclerview.BaseQuickAdapter;
import com.aaron.toolsaaron.recyclerview.BaseViewHolder;
import com.aaron.toolsaaron.utils.GlideUtils;
import com.aaron.toolsaaron.utils.TimeUtils;
import com.aaron.toolsaaron.utils.UIUtils;

import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻详情页评论的适配器
 * @date 2017/6/28  16:01
 */

public class CommentAdapter extends BaseQuickAdapter<CommentData, BaseViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<CommentData> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentData commentData) {
        GlideUtils.loadRound(mContext, commentData.comment.user_profile_image_url, (ImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, commentData.comment.user_name)
                .setText(R.id.tv_like_count, String.valueOf(commentData.comment.digg_count))
                .setText(R.id.tv_content, commentData.comment.text)
                .setText(R.id.tv_time, TimeUtils.getShortTime(commentData.comment.create_time * 1000));

        helper.getView(R.id.tv_like_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showToast("点赞，敬请期待！");
            }
        });
    }
}
