package com.gnusl.actine.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.model.Profile;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.ProfilesAdapter;
import com.gnusl.actine.ui.adapter.ViewPagerAdapter;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MoreFragment extends Fragment implements View.OnClickListener, ProfileClick, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvProfiles;
    private ProfilesAdapter profilesAdapter;

    private Button btnManageProfile;

    private TextView tvMyList, tvHelp, tvLogout, tvAppSetting, tvAccount;
    private TabLayout tlMainTabLayout;
    private ViewPager vpMainContainer;
    private ViewPagerAdapter adapter;
    private MyMoviesFragment myMoviesFragment;
    private MySeriesFragment mySeriesFragment;

    public MoreFragment() {
    }

    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_more, container, false);
            init();
        } else {
            ViewGroup parent = (ViewGroup) inflatedView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
        return inflatedView;
    }

    private void init() {

        DataLoader.getRequest(Urls.Profiles.getLink(), this);

        findViews();

        setupViewPager(vpMainContainer);
        vpMainContainer.setOffscreenPageLimit(3);

        tlMainTabLayout.setupWithViewPager(vpMainContainer);

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
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        profilesAdapter = new ProfilesAdapter(getActivity(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvProfiles.setLayoutManager(layoutManager);

        rvProfiles.setAdapter(profilesAdapter);

    }

    private void findViews() {
        rvProfiles = inflatedView.findViewById(R.id.rv_profiles);
        btnManageProfile = inflatedView.findViewById(R.id.btn_manage_profile);
//        tvMyList = inflatedView.findViewById(R.id.tv_my_list);
//        tvHelp = inflatedView.findViewById(R.id.tv_help);
//        tvLogout = inflatedView.findViewById(R.id.tv_logout);
//        tvAppSetting = inflatedView.findViewById(R.id.tv_app_setting);
//        tvAccount = inflatedView.findViewById(R.id.tv_account);
        tlMainTabLayout = inflatedView.findViewById(R.id.tl_main_tab_layout);
        vpMainContainer = inflatedView.findViewById(R.id.vp_main_container);

        btnManageProfile.setOnClickListener(this);
//        tvMyList.setOnClickListener(this);
//        tvHelp.setOnClickListener(this);
//        tvLogout.setOnClickListener(this);
//        tvAppSetting.setOnClickListener(this);
//        tvAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_manage_profile: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.ManageProfilesExtra.getConst(), (ArrayList<? extends Parcelable>) profilesAdapter.getList());
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ManageProfileFragment, bundle, null);
                    }
                }
                break;
            }
//            case R.id.tv_my_list: {
//                if (getActivity() != null) {
//                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
//                    if (fragment instanceof MoreContainerFragment) {
//                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.MyMoviesFragment, null);
//                    }
//                }
//                break;
//            }
//            case R.id.tv_help: {
//                if (getActivity() != null) {
//                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
//                    if (fragment instanceof MoreContainerFragment) {
//                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.HelpFragment, null);
//                    }
//                }
//                break;
//            }
//            case R.id.tv_app_setting: {
//                if (getActivity() != null) {
//                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
//                    if (fragment instanceof MoreContainerFragment) {
//                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.AppSettingsFragment, null);
//                    }
//                }
//                break;
//            }
//            case R.id.tv_account: {
//                Intent intent = new Intent(getActivity(), AccountActivity.class);
//                intent.putExtra("url",Urls.Account.getLink());
//                startActivity(intent);
//                break;
//            } case R.id.tv_logout: {
//                showLogoutDialog();
//                break;
//            }
        }
    }

    private void showLogoutDialog() {
        final Dialog logoutDialog = new Dialog(getActivity());
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
                        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        SharedPreferencesUtils.clear();
                        startActivity(new Intent(getActivity(), AuthActivity.class));
                        getActivity().finish();
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
    public void onClickProfile(Profile profile, ImageView ivProfile, boolean isLongClick) {
        if (getActivity() != null) {
            if (isLongClick) {

                Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                if (fragment instanceof MoreContainerFragment) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.EditNewProfileExtra.getConst(), profile);
                    bundle.putString("transition", ViewCompat.getTransitionName(ivProfile));
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment, bundle, ivProfile);
                }
            } else {
                mySeriesFragment.refreshData();
                myMoviesFragment.refreshData();
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("profiles")) {
            List<Profile> profiles = Profile.newList(jsonObject.optJSONArray("profiles"));
            profilesAdapter.setList(profiles);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DataLoader.getRequest(Urls.Profiles.getLink(), this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        if (myMoviesFragment == null)
            myMoviesFragment = MyMoviesFragment.newInstance();
        if (mySeriesFragment == null)
            mySeriesFragment = MySeriesFragment.newInstance();
        adapter.addFragment(myMoviesFragment, "My Movies");
        adapter.addFragment(mySeriesFragment, "My Series");
        adapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(adapter);

    }
}
