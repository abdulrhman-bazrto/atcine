package com.gnusl.actine.ui.TV.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.TVTabClick;
import com.gnusl.actine.model.TabObject;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.AuthActivity;
import com.gnusl.actine.ui.Mobile.adapter.GenresAdapter;
import com.gnusl.actine.ui.Mobile.adapter.MainFragmentPagerAdapter;
import com.gnusl.actine.ui.Mobile.adapter.TabsAdapter;
import com.gnusl.actine.ui.Mobile.fragment.HomeFragment;
import com.gnusl.actine.ui.TV.fragment.TVHelpFragment;
import com.gnusl.actine.ui.TV.fragment.TVLoginFragment;
import com.gnusl.actine.ui.TV.fragment.TVMainAuthFragment;
import com.gnusl.actine.ui.TV.fragment.TVManageProfileFragment;
import com.gnusl.actine.ui.TV.fragment.TVMyListFragment;
import com.gnusl.actine.ui.TV.fragment.TVMoviesFragment;
import com.gnusl.actine.ui.TV.fragment.TVPaymentLessFragment;
import com.gnusl.actine.ui.TV.fragment.TVRegisterFragment;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TVMainActivity extends AppCompatActivity implements View.OnClickListener, TVTabClick {
    private RecyclerView rvTabs;
    private TabsAdapter tabsAdapter;
    private Fragment mCurrentFragment;
    private EditText etSearch;
    private ConstraintLayout clTop;
    ImageView ivProfile, ivNotification, ivLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);

        String languageToLoad = SharedPreferencesUtils.getLanguage(Atcine.getApplicationInstance());
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_tvmain);
        init();

    }

    private void init() {

        findViews();
        changeSelectedProfileImg();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        ArrayList<TabObject> tabs = new ArrayList<>();
//        tabs.add(new TabObject("Home",R.drawable.icon_account));
        tabs.add(new TabObject("Movies", R.drawable.ic_movies, true));
        tabs.add(new TabObject("Series", R.drawable.ic_series, false));
        tabs.add(new TabObject("MyList", R.drawable.ic_empty_heart, false));
        tabsAdapter = new TabsAdapter(TVMainActivity.this, tabs, this);

        rvTabs.setLayoutManager(gridLayoutManager);
        rvTabs.setAdapter(tabsAdapter);
        replaceFragment(FragmentTags.TVMoviesFragment);

    }

    private void findViews() {
        rvTabs = findViewById(R.id.rv_tabs);
        clTop = findViewById(R.id.cl_top);
        ivNotification = findViewById(R.id.iv_notifications);
        ivProfile = findViewById(R.id.iv_profile);
        ivLogout = findViewById(R.id.iv_log_out);
        etSearch = findViewById(R.id.et_search);

        ivProfile.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(int position) {
        switch (position) {
            case 0:
                replaceFragment(FragmentTags.TVMoviesFragment);
                break;
            case 1:
                replaceFragment(FragmentTags.TVSeriesFragment);
                break;
            case 2:
                replaceFragment(FragmentTags.MyListFragment);
                break;
        }
    }

    public void replaceFragment(FragmentTags fragmentTags) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case TVMoviesFragment:

                mCurrentFragment = TVMoviesFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment);
                transaction.commit();

                break;

            case TVSeriesFragment:

                mCurrentFragment = TVLoginFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment);
                transaction.commit();

                break;

            case MyListFragment:

                mCurrentFragment = TVMyListFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;
            case ManageProfileFragment:

                mCurrentFragment = TVManageProfileFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile:
                replaceFragment(FragmentTags.ManageProfileFragment);
                break;
            case R.id.iv_log_out:
                showLogoutDialog();
                break;
        }
    }

    public void changeSearchVisibility(boolean shouldHide) {
        if (shouldHide) {
            etSearch.setVisibility(View.GONE);
            clTop.setVisibility(View.GONE);
        } else {
            etSearch.setVisibility(View.VISIBLE);
            clTop.setVisibility(View.VISIBLE);
            changeSelectedProfileImg();
        }
    }

    private void changeSelectedProfileImg() {
        if (!SharedPreferencesUtils.getCurrentProfileImageUrl().isEmpty()) {
            Picasso.with(TVMainActivity.this).load(SharedPreferencesUtils.getCurrentProfileImageUrl()).placeholder(getDrawable(R.drawable.icon_account1)).into(ivProfile);
        }else
            ivProfile.setImageDrawable(getDrawable(R.drawable.icon_account1));
    }

    private void showLogoutDialog() {
        final Dialog logoutDialog = new Dialog(TVMainActivity.this);
        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (logoutDialog.getWindow() != null)
            logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.setContentView(R.layout.dialog_logout);
        logoutDialog.setCancelable(true);

        logoutDialog.findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
                DataLoader.postRequest(Urls.Logout.getLink(), new HashMap<>(), new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {
                        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        SharedPreferencesUtils.clear();
                        startActivity(new Intent(TVMainActivity.this, TVAuthActivity.class));
                        TVMainActivity.this.finish();
                    }
                });
            }
        });

        logoutDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });

        logoutDialog.show();
    }
}
