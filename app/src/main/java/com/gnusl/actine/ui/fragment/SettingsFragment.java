package com.gnusl.actine.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AccountActivity;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.HashMap;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private TextView tvHelp, tvLogout, tvAppSetting, tvAccount;
    private ConstraintLayout clRoot;
    Animation animation;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_settings, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        findViews();
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_side);
    }

    private void findViews() {
        tvHelp = inflatedView.findViewById(R.id.tv_help);
        tvLogout = inflatedView.findViewById(R.id.tv_logout);
        tvAppSetting = inflatedView.findViewById(R.id.tv_app_setting);
        tvAccount = inflatedView.findViewById(R.id.tv_account);
        clRoot = inflatedView.findViewById(R.id.root_view);

        tvHelp.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvAppSetting.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_help: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.HelpFragment, null, null);
                    }
                }
                break;
            }
            case R.id.tv_app_setting: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.AppSettingsFragment, null, null);
                    }
                }
                break;
            }
            case R.id.tv_account: {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                intent.putExtra("url", Urls.Account.getLink());
                startActivity(intent);
                break;
            }
            case R.id.tv_logout: {
                showLogoutDialog();
                break;
            }

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

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            animation.setDuration(1000);
//            animation.setStartOffset(2000);
////            animation.setFillAfter(true);
////            animation.setFillEnabled(true);
//            tvHelp.startAnimation(animation);
//            tvAccount.startAnimation(animation);
//            tvAppSetting.startAnimation(animation);
//            tvLogout.startAnimation(animation);
//        }
//    }

    public void startAnimation() {
        animation.setDuration(1200);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        clRoot.startAnimation(animation);
//        tvAccount.startAnimation(animation);
//        tvLogout.startAnimation(animation);
    }
}
