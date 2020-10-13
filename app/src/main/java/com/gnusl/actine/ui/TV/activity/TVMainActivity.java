package com.gnusl.actine.ui.TV.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.TVTabClick;
import com.gnusl.actine.model.TabObject;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.adapter.TabsAdapter;
import com.gnusl.actine.ui.Mobile.fragment.DownloadFragment;
import com.gnusl.actine.ui.Mobile.fragment.HomeContainerFragment;
import com.gnusl.actine.ui.Mobile.fragment.HomeFragment;
import com.gnusl.actine.ui.Mobile.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.Mobile.fragment.SearchContainerFragment;
import com.gnusl.actine.ui.Mobile.fragment.SearchResultFragment;
import com.gnusl.actine.ui.TV.fragment.TVManageProfileFragment;
import com.gnusl.actine.ui.TV.fragment.TVMoviesContainerFragment;
import com.gnusl.actine.ui.TV.fragment.TVMyListContainerFragment;
import com.gnusl.actine.ui.TV.fragment.TVMyListFragment;
import com.gnusl.actine.ui.TV.fragment.TVMoviesFragment;
import com.gnusl.actine.ui.TV.fragment.TVSearchResultFragment;
import com.gnusl.actine.ui.TV.fragment.TVSeriesContainerFragment;
import com.gnusl.actine.ui.TV.fragment.TVSeriesFragment;
import com.gnusl.actine.ui.TV.fragment.TVShowDetailsFragment;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
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
        setFragmentView();


    }

    private void findViews() {
        rvTabs = findViewById(R.id.rv_tabs);
        clTop = findViewById(R.id.cl_top);
        ivNotification = findViewById(R.id.iv_notifications);
        ivProfile = findViewById(R.id.iv_profile);
        ivLogout = findViewById(R.id.iv_log_out);
        etSearch = findViewById(R.id.et_search);

        Utils.setOnFocusScale(ivLogout);
        Utils.setOnFocusScale(ivProfile);

        ivProfile.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(int position) {
        switch (position) {
            case 0:
                replaceFragment(FragmentTags.TVMoviesContainerFragment, null);
                break;
            case 1:
                replaceFragment(FragmentTags.TVSeriesContainerFragment, null);
                break;
            case 2:
                replaceFragment(FragmentTags.TVMyListContainerFragment, null);
                break;
        }
    }

    public void replaceFragment(FragmentTags fragmentTags, Bundle bundle) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case TVMoviesFragment:

                mCurrentFragment = TVMoviesFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment);
                transaction.commit();

                break;

            case TVMoviesContainerFragment:

                mCurrentFragment = TVMoviesContainerFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment);
                transaction.commit();

                break;

            case TVSeriesFragment:

                mCurrentFragment = TVSeriesFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment);
                transaction.commit();

                break;

            case TVSeriesContainerFragment:

                mCurrentFragment = TVSeriesContainerFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment);
                transaction.commit();

                break;

            case MyListFragment:

                mCurrentFragment = TVMyListFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;

            case TVMyListContainerFragment:

                mCurrentFragment = TVMyListContainerFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;

            case ManageProfileFragment:

                mCurrentFragment = TVManageProfileFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;

            case SearchResultFragment:

                mCurrentFragment = TVSearchResultFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case ShowDetailsFragment:

                mCurrentFragment = TVShowDetailsFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_tv_main, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

        }
    }

    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile:
                replaceFragment(FragmentTags.ManageProfileFragment, null);
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
        } else
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

    @Override
    public void onBackPressed() {
        try {
            Fragment fragment = getmCurrentFragment();
            if (fragment instanceof TVMoviesContainerFragment) {
                FragmentManager fm = fragment.getChildFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                    ((TVMoviesContainerFragment) fragment).getFragmentStack().pop();
                    if (((TVMoviesContainerFragment) fragment).getFragmentStack().peek() instanceof HomeFragment) {
                        if (!((TVMoviesContainerFragment) fragment).getFragmentStack().empty()) {
                            HomeFragment homeFragment = (HomeFragment) ((TVMoviesContainerFragment) fragment).getFragmentStack().peek();
                            homeFragment.refreshTrendShow();
                        }
                    }
                } else {
                    super.onBackPressed();
                }
            }
            if (fragment instanceof TVSeriesContainerFragment) {
                FragmentManager fm = fragment.getChildFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    setFragmentView();
                }
            }
            if (fragment instanceof TVMyListContainerFragment) {
                FragmentManager fm = fragment.getChildFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    setFragmentView();
                }
            }
//            if (fragment instanceof TVManageProfileFragment) {
//                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            }
        } catch (Exception e) {

        }
    }

    private void setFragmentView() {
        ArrayList<TabObject> tabs = new ArrayList<>();
        tabs.add(new TabObject("Movies", R.drawable.ic_movies, true));
        tabs.add(new TabObject("Series", R.drawable.ic_series, false));
        tabs.add(new TabObject("MyList", R.drawable.ic_empty_heart, false));
        tabsAdapter = new TabsAdapter(TVMainActivity.this, tabs, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvTabs.setLayoutManager(gridLayoutManager);
        rvTabs.setAdapter(tabsAdapter);
        replaceFragment(FragmentTags.TVMoviesContainerFragment, null);
    }

}
