package com.aaron.fangtoutiao.uis.activitys;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.uis.fragments.BigImageFragment;
import com.aaron.toolsaaron.adapter.BasePagerAdapter;
import com.aaron.toolsaaron.listener.PermissionListener;
import com.aaron.toolsaaron.ui.base.BaseActivity;
import com.aaron.toolsaaron.ui.base.BaseFragment;
import com.aaron.toolsaaron.ui.base.BasePresenter;
import com.aaron.toolsaaron.utils.FileUtils;
import com.aaron.toolsaaron.utils.LogUtil;
import com.aaron.toolsaaron.utils.UIUtils;
import com.aaron.toolsaaron.views.PickViewPager;
import com.aaron.toolsaaron.viewtools.statusbar.Eyes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * @des 图片查看
 */
public class ImageViewPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String IMG_URLS = "mImageUrls";
    public static final String POSITION = "position";

    @Bind(R.id.vp_pics)
    PickViewPager mVpPics;

    @Bind(R.id.tv_indicator)
    TextView mTvIndicator;

    @Bind(R.id.tv_save)
    TextView mTvSave;

    // 图片集合
    private List<String> mImageUrls = new ArrayList<String>();
    private int mCurrentPosition;
    // 图片显示Fragment
    private List<BaseFragment> mFragments = new ArrayList<>();
    private Map<Integer, Boolean> mDownloadingFlagMap = new HashMap<>();//用于保存对应位置图片是否在下载的标识

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_image_view_pager;
    }

    @Override
    public void initView() {
        Eyes.translucentStatusBar(this);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mImageUrls = intent.getStringArrayListExtra(IMG_URLS);
        // 默认第几张图片
        mCurrentPosition = intent.getIntExtra(POSITION, 0);
        for (int i = 0; i < mImageUrls.size(); i++) {
            String url = mImageUrls.get(i);
            BigImageFragment imageFragment = new BigImageFragment();

            Bundle bundle = new Bundle();
            bundle.putString(BigImageFragment.IMG_URL, url);
            imageFragment.setArguments(bundle);

            mFragments.add(imageFragment);//添加到fragment集合中
            mDownloadingFlagMap.put(i, false);//初始化map，一开始全部的值都为false
        }

        mVpPics.setAdapter(new BasePagerAdapter(mFragments, getSupportFragmentManager()));
        mVpPics.setOffscreenPageLimit(mImageUrls.size());
        mVpPics.addOnPageChangeListener(this);

        mVpPics.setCurrentItem(mCurrentPosition);
        setIndicator();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        //页面变化时，设置当前的指示
        setIndicator();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setIndicator() {
        mTvIndicator.setText(mCurrentPosition + 1 + "/" + mImageUrls.size());//设置当前的指示
    }

    @OnClick(R.id.tv_save)
    public void onViewClicked() {
        requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                // 保存图片
                downloadImg();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                UIUtils.showToast(getString(R.string.write_storage_permission_deny));
            }
        });
    }

    private void downloadImg() {
        String url = mImageUrls.get(mCurrentPosition);
        boolean isDownlading = mDownloadingFlagMap.get(mCurrentPosition);
        if (!isDownlading) {
            // 如果不是正在下载，则开始下载
            mDownloadingFlagMap.put(mCurrentPosition, true);// 更改标识为 正在下载

            new DownloadImgTask(mCurrentPosition).execute(url);
        }
    }

    /**
     * 下载任务
     */
    class DownloadImgTask extends AsyncTask<String, Integer, Void> {

        private int mPosition;

        public DownloadImgTask(int position) {
            mPosition = position;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String imageUrl = strings[0];
            File file = null;
            try {
                FutureTarget<File> futureTarget = Glide.with(ImageViewPagerActivity.this)
                        .load(imageUrl)
                        .downloadOnly(SIZE_ORIGINAL, SIZE_ORIGINAL);

                file = futureTarget.get();

                String filePath = file.getAbsolutePath();

                String destFileName = System.currentTimeMillis() + FileUtils.getImageFileExt(filePath);
                File destFile = new File(FileUtils.getDir(""), destFileName);

                FileUtils.copy(file, destFile);// 保存图片

                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(destFile.getPath()))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LogUtil.e("正在下载图片 progress = " + values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // 保存成功
            mDownloadingFlagMap.put(mCurrentPosition, false);
            LogUtil.e("保存成功，图片所在文件夹:SD卡根路径/" + FileUtils.APP_STORAGE_ROOT);
            UIUtils.showToast("图片已保存！");
        }
    }
}
