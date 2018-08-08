package com.aaron.toolsaaron.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aaron.toolsaaron.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 和viewPager配套的adapter
 */
public class BasePagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();

    public BasePagerAdapter(List<BaseFragment> fragmentList, FragmentManager fm) {
        super(fm);
        if (fragmentList != null) {
            mFragments = fragmentList;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
