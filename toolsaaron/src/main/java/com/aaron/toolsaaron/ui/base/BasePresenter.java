package com.aaron.toolsaaron.ui.base;

/**
 * @author Aaron
 * @date 2018/08/06 16:59
 * @description MVP 模式中的P
 */
public abstract class BasePresenter<V> {
    protected V mView;

    public BasePresenter(V view) {
        attachView(view);
    }

    public void attachView(V view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
//        onUnsubscribe();
    }
}
