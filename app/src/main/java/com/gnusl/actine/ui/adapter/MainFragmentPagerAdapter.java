package com.gnusl.actine.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
        }
//        else if (position == 2) {
//            return ComingSoonContainerFragment.newInstance();
//        }
        else if (position == 2) {
            return DownloadFragment.newInstance();
        } else
            return MoreContainerFragment.newInstance();
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
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
