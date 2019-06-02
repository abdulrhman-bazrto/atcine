package com.gnusl.actine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.adapter.MainFragmentPagerAdapter;
import com.gnusl.actine.ui.fragment.DownloadFragment;
import com.gnusl.actine.ui.fragment.HomeContainerFragment;
import com.gnusl.actine.ui.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.fragment.SearchContainerFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class MainActivity extends AppCompatActivity implements SmartTabLayout.TabProvider {

    private Fragment mCurrentFragment;
    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager vpHome;
    private SmartTabLayout tlHome;
    private int selectedPosition;
    private FragmentTags selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {

        findViews();

        pagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());
        vpHome.setAdapter(pagerAdapter);
        tlHome.setCustomTabView(this);
        tlHome.setViewPager(vpHome);

        //default state is home fragment
        setFragmentView(0);
        selectedPosition = 0;
        tlHome.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                setFragmentView(position);
                selectedPosition = position;
            }
        });

    }

    private void findViews() {
        tlHome = findViewById(R.id.tl_home);
        vpHome = findViewById(R.id.vp_home);

    }

    private void setFragmentView(int position) {
        this.selectedFragment = getFragmentTagByPosition(position);
        if (vpHome != null)
            vpHome.setCurrentItem(position);

        View defaultView = tlHome.getTabAt(selectedPosition);
        AppCompatImageView ivIcon = defaultView.findViewById(R.id.iv_icon);
        TextView tvTitle = defaultView.findViewById(R.id.tv_title);
        switch (selectedPosition) {
            case 0:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_gray));
                tvTitle.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case 1:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_search_gray));
                tvTitle.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case 2:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_coming_soon_gray));
                tvTitle.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case 3:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_download_gray));
                tvTitle.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case 4:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_more_gray));
                tvTitle.setTextColor(getResources().getColor(R.color.gray2));

                break;
        }
        selectedPosition = position;
        View view = tlHome.getTabAt(position);
        if (view != null) {
            AppCompatImageView icon = view.findViewById(R.id.iv_icon);
            TextView title = view.findViewById(R.id.tv_title);
            switch (position) {
                case 0:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_white));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 1:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_search_white));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 2:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_coming_soon_white));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 3:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_download_white));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 4:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_more_white));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
            }
        }
    }

    private FragmentTags getFragmentTagByPosition(int position) {

        switch (position) {

            case 0:
                return FragmentTags.HomeContainerFragment;

            case 1:
                return FragmentTags.SearchFragment;

            case 2:
                return FragmentTags.ComingSoonFragment;

            case 3:
                return FragmentTags.DownloadsFragment;

            default:
                return FragmentTags.MoreFragment;
        }
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(this);

        View inflatedView = inflater.inflate(R.layout.item_custom_tab_view, container, false);

        AppCompatImageView ivIcon = inflatedView.findViewById(R.id.iv_icon);
        TextView tvTitle = inflatedView.findViewById(R.id.tv_title);

        switch (position) {
            case 0:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_gray));
                tvTitle.setText("Home");
                break;

            case 1:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_search_gray));
                tvTitle.setText("Search");
                break;

            case 2:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_coming_soon_gray));
                tvTitle.setText("Coming Soon");
                break;

            case 3:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_download_gray));
                tvTitle.setText("Download");
                break;

            case 4:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_more_gray));
                tvTitle.setText("More");
                break;

        }

        return inflatedView;
    }

    public Fragment getmCurrentFragment() {
        return pagerAdapter.getCurrentFragment();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getmCurrentFragment();
        if (fragment instanceof HomeContainerFragment) {
            FragmentManager fm = fragment.getChildFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
        if (fragment instanceof SearchContainerFragment) {
            FragmentManager fm = fragment.getChildFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                setFragmentView(0);
            }
        }
        if (fragment instanceof MoreContainerFragment) {
            FragmentManager fm = fragment.getChildFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                setFragmentView(0);
            }
        }
        if (fragment instanceof DownloadFragment) {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (pagerAdapter.getCurrentFragment() instanceof MoreContainerFragment)
            ((MoreContainerFragment) pagerAdapter.getCurrentFragment()).getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }
}
