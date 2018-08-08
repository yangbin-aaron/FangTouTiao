package com.aaron.fangtoutiao.uis.activitys;

import com.aaron.fangtoutiao.R;
import com.aaron.fangtoutiao.model.event.TabRefreshCompletedEvent;
import com.aaron.fangtoutiao.uis.fragments.HomeFragment;
import com.aaron.fangtoutiao.uis.fragments.MicroFragment;
import com.aaron.fangtoutiao.uis.fragments.MineFragment;
import com.aaron.fangtoutiao.uis.fragments.VideoFragment;
import com.aaron.toolsaaron.adapter.BasePagerAdapter;
import com.aaron.toolsaaron.ui.base.BaseActivity;
import com.aaron.toolsaaron.ui.base.BaseFragment;
import com.aaron.toolsaaron.ui.base.BasePresenter;
import com.aaron.toolsaaron.utils.UIUtils;
import com.aaron.toolsaaron.views.NoScrollViewPager;
import com.aaron.toolsaaron.viewtools.bottomBar.BottomBarItem;
import com.aaron.toolsaaron.viewtools.bottomBar.BottomBarLayout;
import com.aaron.toolsaaron.viewtools.statusbar.Eyes;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Bind(R.id.vp_content)
    NoScrollViewPager mVpContent;
    @Bind(R.id.bottom_bar)
    BottomBarLayout mBottomBarLayout;

    private List<BaseFragment> mFragments;
    private BasePagerAdapter mPagerAdapter;

    // 头部状态栏的背景颜色
    private int[] mStatusColors = new int[]{
            R.color.colorPrimary,
            R.color.white,
            R.color.white,
    };

    @Override
    public String getMainAppName() {
        return getString(R.string.app_name);
    }

    @Override
    public void initData() {
        super.initData();
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new VideoFragment());
        mFragments.add(new MicroFragment());
        mFragments.add(new MineFragment());
    }

    @Override
    public void initListener() {
        super.initListener();
        mPagerAdapter = new BasePagerAdapter(mFragments, getSupportFragmentManager());
        mVpContent.setAdapter(mPagerAdapter);
        // 预加载
        mVpContent.setOffscreenPageLimit(mFragments.size());
        // 和底部tab绑定
        mBottomBarLayout.setViewPager(mVpContent);
        // 设置条目点击的监听
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int position) {
                setStatusBarColor(position);//设置状态栏颜色

//                JCVideoPlayer.releaseAllVideos();//底部页签切换或者是下拉刷新，释放资源

                if (position == 0 || position == 1) {
                    //如果点击的是首页和视频（都有下拉刷新）
                    if (mBottomBarLayout.getCurrentItem() == position) {
                        //如果当前页码和点击的页码一致,进行下拉刷新
                        String channelCode = "";
                        if (position == 0) {
                            channelCode = ((HomeFragment) mFragments.get(0)).getCurrentChannelCode();//获取到首页当前显示的fragment的频道
                        } else {
                            channelCode = ((VideoFragment) mFragments.get(1)).getCurrentChannelCode();//获取到视频当前显示的fragment的频道
                        }
//                        postTabRefreshEvent(bottomBarItem, position, channelCode);//发送下拉刷新的事件
                    }
                    return;
                }

                // 点击首页的按钮时，如果刷新，图标会变成刷新的加载图标，所以需要换回来
//                //如果点击了其他条目
//                BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);
//                bottomItem.setIconSelectedResourceId(R.drawable.tab_home_selected);//更换为原来的图标
//
//                cancelTabLoading(bottomItem);//停止旋转动画
            }
        });
    }

    /**
     * 设置状态栏的颜色
     *
     * @param position
     */
    private void setStatusBarColor(int position) {
        if (position == 3) {
            //如果是我的页面，状态栏设置为透明状态栏（页面也会整体上移到状态栏位置）
            Eyes.translucentStatusBar(this, true);
        } else {
            Eyes.setStatusBarColor(this, UIUtils.getColor(mStatusColors[position]));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshCompletedEvent(TabRefreshCompletedEvent event) {
        //接收到刷新完成的事件，取消旋转动画，更换底部首页页签图标
//        BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);
//
//        cancelTabLoading(bottomItem);//停止旋转动画
//
//        bottomItem.setIconSelectedResourceId(R.mipmap.tab_home_selected);//更换成首页原来图标
//        bottomItem.setStatus(true);//刷新图标
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus(this);
    }

    @Override
    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JCVideoPlayer.releaseAllVideos();
    }
}
