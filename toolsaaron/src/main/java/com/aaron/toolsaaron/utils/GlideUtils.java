package com.aaron.toolsaaron.utils;

import android.content.Context;
import android.widget.ImageView;

import com.aaron.toolsaaron.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {
    public static void load(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_default);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void load(Context context, String url, ImageView iv, int placeHolderResId) {
        if (placeHolderResId == -1) {
            Glide.with(context)
                    .load(url)
                    .into(iv);
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolderResId);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void loadRound(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_circle_default)
                .centerCrop()
                .circleCrop();

        Glide.with(context)//
                .load(url)//
                .apply(options)//
                .into(iv);
    }
}
