package com.aaron.toolsaaron.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义一个不能左右滑动的ViewPager
 *
 * @author Beyond
 */
public class NoScrollViewPager extends ViewPager {

    /**
     * 能否左右滑动
     */
    private boolean mCanScroll = false;

    /**
     * 设置为true 可以滑动
     *
     * @param canScroll
     */
    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (!mCanScroll) {
            try {
                return super.onTouchEvent(arg0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return true;// 可行,消费,拦截事件
        }
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子控件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // return false;//可行,不拦截事件,
        // return true;//不行,子控件无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        if (!mCanScroll) {
            try {
                return super.onInterceptTouchEvent(arg0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子控件都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
