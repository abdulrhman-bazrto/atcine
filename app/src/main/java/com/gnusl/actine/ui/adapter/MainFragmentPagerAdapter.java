package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gnusl.actine.ui.fragment.ComingSoonContainerFragment;
import com.gnusl.actine.ui.fragment.DownloadFragment;
import com.gnusl.actine.ui.fragment.HomeContainerFragment;
import com.gnusl.actine.ui.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.fragment.SearchContainerFragment;


public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment mCurrentFragment;

    public MainFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return HomeContainerFragment.newInstance();
        } else if (position == 1) {
            return SearchContainerFragment.newInstance();
        } else if (position == 2) {
            return ComingSoonContainerFragment.newInstance();
        } else if (position == 3) {
            return DownloadFragment.newInstance();
        } else
            return MoreContainerFragment.newInstance();
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 5;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void setCurrentFragment(Fragment fragment) {
        this.mCurrentFragment = fragment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Home";
    }

}
