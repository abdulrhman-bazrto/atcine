package com.gnusl.actine.ui.TV.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.User;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.AuthActivity;
import com.gnusl.actine.ui.Mobile.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.ui.Mobile.fragment.MyMoviesFragment;
import com.gnusl.actine.ui.Mobile.fragment.MySeriesFragment;
import com.gnusl.actine.ui.Mobile.fragment.SettingsFragment;
import com.gnusl.actine.ui.TV.activity.TVMainActivity;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.HashMap;


public class TVMyListFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {
    View inflatedView;

    private Button btnLogin;
    private EditText etEmailPhone, etPassword;
    private TextView btn_language,  tvWelcome, tvForget, tvTest, tvTest1;
    private ImageView ivLogo;
    private ConstraintLayout clMain;

    private TabLayout tlMainTabLayout;
    private ViewPager vpMainContainer;
    private ViewPagerAdapter adapter;
    View mIndicator;
    private int indicatorWidth;
    private TVMyMoviesFragment myMoviesFragment;
    private TVMySeriesFragment mySeriesFragment;

    public TVMyListFragment() {
    }

    public static TVMyListFragment newInstance() {
        TVMyListFragment fragment = new TVMyListFragment();
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
        inflatedView = inflater.inflate(R.layout.fragment_tvmy_list, container, false);
        init();
//        }
        return inflatedView;
    }
    private void init() {
        findViews();

        setupViewPager(vpMainContainer);
        vpMainContainer.setOffscreenPageLimit(3);

        tlMainTabLayout.setupWithViewPager(vpMainContainer);

        //Determine indicator width at runtime
        tlMainTabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = tlMainTabLayout.getWidth() / tlMainTabLayout.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        tlMainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                changeTabTitle(position);
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
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 0)
//                    myMoviesFragment.startanimation();
//                if (position == 1)
//                    mySeriesFragment.startanimation();

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//        btnLogin.setOnClickListener(this);
//        Utils.setOnFocusScale(btnLogin);

    }
    private void findViews() {
        tlMainTabLayout = inflatedView.findViewById(R.id.tl_main_tab_layout);
        vpMainContainer = inflatedView.findViewById(R.id.vp_main_container);
        mIndicator = inflatedView.findViewById(R.id.indicator);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onConnectionError(int code, String message) {
    }

    @Override
    public void onConnectionError(ANError anError) {
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        if (myMoviesFragment == null)
            myMoviesFragment = TVMyMoviesFragment.newInstance();
        if (mySeriesFragment == null)
            mySeriesFragment = TVMySeriesFragment.newInstance();
        adapter.addFragment(myMoviesFragment, getActivity().getString(R.string.movies));
        adapter.addFragment(mySeriesFragment, getActivity().getString(R.string.tv_series));
//        adapter.addFragment(new SettingsFragment(), getActivity().getString(R.string.settings));
        viewPager.setAdapter(adapter);

    }
}
