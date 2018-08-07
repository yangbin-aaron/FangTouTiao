package com.aaron.toolsaaron.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.toolsaaron.R;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


public abstract class BaseFragment<T extends BasePresenter> extends VisFragment {
    // MVP
    protected T mPresenter;
    // 跟布局
    private View rootView;
    protected StateView mStateView;//用于显示加载中、网络异常，空布局、内容布局
    // activity
    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            int layoutId = provideContentViewId();
            if (layoutId == 0) return super.onCreateView(inflater, container, savedInstanceState);

            rootView = inflater.inflate(layoutId, container, false);
            ButterKnife.bind(this, rootView);

            mStateView = StateView.inject(getStateViewRoot());
            if (mStateView != null) {
                mStateView.setLoadingResource(R.layout.page_loading);
                mStateView.setRetryResource(R.layout.page_net_error);
            }

            initView(rootView);
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     */
    public View getStateViewRoot() {
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * 初始化一些view
     *
     * @param rootView
     */
    public void initView(View rootView) {
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置listener的操作
     */
    public void initListener() {

    }

    @Override
    protected void onFragmentFirstVisible() {
        //当第一次可见的时候，加载数据
        loadData();
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    //加载数据
    protected abstract void loadData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        rootView = null;
    }

    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }
}
