package com.gnusl.actine.ui.TV.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.Mobile.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.Mobile.custom.NonScrollHomeViewPager1;
import com.gnusl.actine.ui.Mobile.fragment.DownloadShowsFragment;
import com.gnusl.actine.ui.Mobile.fragment.EpisodesFragment;
import com.gnusl.actine.ui.Mobile.fragment.OverviewFragment;
import com.gnusl.actine.ui.Mobile.fragment.ReviewsFragment;
import com.gnusl.actine.ui.Mobile.fragment.TrailerFragment;
import com.google.android.material.tabs.TabLayout;


public class TVMainAuthFragment extends Fragment {

    View inflatedView;
    private TabLayout tlMainTabLayout;
    private NonScrollHomeViewPager1 vpMainContainer;
    private ViewPagerAdapter adapter;
    private TextView tvWelcome, tvWelcome1;

    public TVMainAuthFragment() {
    }

    public static TVMainAuthFragment newInstance() {
        TVMainAuthFragment fragment = new TVMainAuthFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
//        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if (inflatedView == null) {
        inflatedView = inflater.inflate(R.layout.fragment_tvmainauth, container, false);
        init();
//        }
        return inflatedView;
    }

    private void init() {
        findViews();

        setupViewPager(vpMainContainer);
        vpMainContainer.setOffscreenPageLimit(2);
        vpMainContainer.shouldAnimate = false;
        tlMainTabLayout.setupWithViewPager(vpMainContainer);

        tlMainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                changeTabTitle(position);
                vpMainContainer.setCurrentItem(position);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpMainContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
//
//                //Multiply positionOffset with indicatorWidth to get translation
//                float translationOffset = (positionOffset + i) * indicatorWidth;
//                params.leftMargin = (int) translationOffset;
//                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                vpMainContainer.reMeasureCurrentPage(vpMainContainer.getCurrentItem());
//                Fragment fragment = (Fragment) adapter.instantiateItem(vpMainContainer, position);
//                if (fragment instanceof TrailerFragment)
//                    ((TrailerFragment) fragment).startAnimation();
//                else if (fragment instanceof OverviewFragment)
//                    ((OverviewFragment) fragment).startAnimation();
//                else if (fragment instanceof ReviewsFragment)
//                    ((ReviewsFragment) fragment).startAnimation();
//                else if (fragment instanceof EpisodesFragment)
//                    ((EpisodesFragment) fragment).startAnimation();

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void changeTabTitle(int position) {
        switch (position) {
            case 0:
                tvWelcome.setText(getActivity().getString(R.string.create_your_account));
                tvWelcome1.setText(getActivity().getString(R.string.sign_up_to_watch));
                break;
            case 1:
                tvWelcome.setText(getActivity().getString(R.string.welcome_back));
                tvWelcome1.setText(getActivity().getString(R.string.we_miss_you_from_the_last_time));
                break;
        }
    }

    private void findViews() {
        tvWelcome = inflatedView.findViewById(R.id.tv_welcome);
        tvWelcome1 = inflatedView.findViewById(R.id.tv_welcome1);
        tlMainTabLayout = inflatedView.findViewById(R.id.tl_main_tab_layout);
        vpMainContainer = inflatedView.findViewById(R.id.vp_main_container);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(TVRegisterFragment.newInstance(), getActivity().getString(R.string.sign_up));
        adapter.addFragment(TVLoginFragment.newInstance(), getActivity().getString(R.string.sign_in));
        viewPager.setAdapter(adapter);

    }
}
