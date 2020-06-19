package com.gnusl.actine.ui.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Profile;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.adapter.MainFragmentPagerAdapter;
import com.gnusl.actine.ui.adapter.ProfileSelectAdapter;
import com.gnusl.actine.ui.fragment.DownloadFragment;
import com.gnusl.actine.ui.fragment.HomeContainerFragment;
import com.gnusl.actine.ui.fragment.HomeFragment;
import com.gnusl.actine.ui.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.fragment.SearchContainerFragment;
import com.gnusl.actine.util.DialogUtils;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity implements SmartTabLayout.TabProvider, ConnectionDelegate {

    private Fragment mCurrentFragment;
    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager vpHome;
    private SmartTabLayout tlHome;
    private int selectedPosition;
    private FragmentTags selectedFragment;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("type")) {
            if (getIntent().getExtras().getString("type").equalsIgnoreCase("stop")) {
                int shoeId = getIntent().getExtras().getInt("id");
                AndroidNetworking.forceCancelAll();
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(shoeId);
            }
        }
//        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("com.gnusl.receiver"));

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

        if (SharedPreferencesUtils.getCurrentProfile() == 0) {
            DataLoader.getRequest(Urls.Profiles.getLink(), this);
        }


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        if (task.getResult() != null) {
                            String token = task.getResult().getToken();
                            HashMap<String, String> body = new HashMap<>();
                            body.put("fcm_token", token);
                            DataLoader.postRequest(Urls.AccountUpdate.getLink(), body, null);
                        }
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("atcine").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                task.isSuccessful();
                Log.d("fcm_topic", String.valueOf(task.isSuccessful()));
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("atcine-android");

        DataLoader.getRequest(Urls.LocationCheck, new HashMap<>(), new ConnectionDelegate() {
            @Override
            public void onConnectionError(int code, String message) {

                DialogUtils.showLocationDialog(MainActivity.this, message);
            }

            @Override
            public void onConnectionError(ANError anError) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {

            }
        });

    }

    public void replaceFragment(int pos) {
        setFragmentView(pos);
        selectedPosition = pos;
    }

    private void findViews() {
        tlHome = findViewById(R.id.tl_home);
        vpHome = findViewById(R.id.vp_home);
        vpHome.setOffscreenPageLimit(0);

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
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_home));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_search));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_downloads));
                tvTitle.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_profile));
                tvTitle.setTextColor(getResources().getColor(R.color.white));

                break;
        }
        selectedPosition = position;
        View view = tlHome.getTabAt(position);
        if (view != null) {
            AppCompatImageView icon = view.findViewById(R.id.iv_icon);
            TextView title = view.findViewById(R.id.tv_title);
            switch (position) {
                case 0:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_filled_home));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 1:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_filled_search));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 2:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_filled_downloads));
                    title.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 3:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_filled_profile));
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
        Utils.setOnFocusScale(inflatedView);
        AppCompatImageView ivIcon = inflatedView.findViewById(R.id.iv_icon);
        TextView tvTitle = inflatedView.findViewById(R.id.tv_title);

        switch (position) {
            case 0:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_home));
                tvTitle.setText("Home");
                break;

            case 1:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_search));
                tvTitle.setText("Search");
                break;

            case 2:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_downloads));
                tvTitle.setText("My Downloads");
                break;

            case 3:
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_profile));
                tvTitle.setText("Profile");
                break;

        }

        return inflatedView;
    }

    public Fragment getmCurrentFragment() {
        return pagerAdapter.getCurrentFragment();
    }


    @Override
    public void onBackPressed() {
        try {
            Fragment fragment = getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                FragmentManager fm = fragment.getChildFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                    ((HomeContainerFragment) fragment).getFragmentStack().pop();
                    if (((HomeContainerFragment) fragment).getFragmentStack().peek() instanceof HomeFragment) {
                        if (!((HomeContainerFragment) fragment).getFragmentStack().empty()) {
                            HomeFragment homeFragment = (HomeFragment) ((HomeContainerFragment) fragment).getFragmentStack().peek();
                            homeFragment.refreshTrendShow();
                        }
                    }
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
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pagerAdapter.getCurrentFragment() instanceof MoreContainerFragment)
            ((MoreContainerFragment) pagerAdapter.getCurrentFragment()).getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionError(int code, String message) {

        if (code == 401) {
            SharedPreferencesUtils.clear();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        Toast.makeText(this, anError.getMessage(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        if (jsonObject.has("profiles")) {
            List<Profile> profiles = Profile.newList(jsonObject.optJSONArray("profiles"));
            if (profiles.size() == 1) {
                SharedPreferencesUtils.saveCurrentProfile(profiles.get(0).getId());
                return;
            } else if (profiles.size() == 0) {
                return;
            } else {
                Dialog profilesDialog = new Dialog(this);
                profilesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                profilesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                profilesDialog.setCancelable(false);
                profilesDialog.setContentView(R.layout.dialog_select_profile);
                profilesDialog.show();

                ProfileSelectAdapter profileSelectAdapter = new ProfileSelectAdapter(this, profilesDialog);

                RecyclerView rvProfiles = profilesDialog.findViewById(R.id.rv_profiles);

                GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

                rvProfiles.setLayoutManager(layoutManager);

                rvProfiles.setAdapter(profileSelectAdapter);

                profileSelectAdapter.setList(profiles);

            }
        }
    }


}
