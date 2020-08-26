package com.gnusl.actine.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import java.util.Locale;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private TextView tvHelp, tvLogout, tvAppSetting, tvAccount,tv_language;
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
        tv_language = inflatedView.findViewById(R.id.tv_language);

        tvHelp.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvAppSetting.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
        tv_language.setOnClickListener(this);
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
            case R.id.tv_language: {
                showLanguageDialog();
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
                        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
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
    private void showLanguageDialog() {
        final Dialog languageDialog = new Dialog(getActivity());
        languageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (languageDialog.getWindow() != null)
            languageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        languageDialog.setContentView(R.layout.dialog_logout);
        languageDialog.setCancelable(true);

        TextView btn_sign_out = languageDialog.findViewById(R.id.btn_sign_out);
        TextView btn_cancel = languageDialog.findViewById(R.id.btn_cancel);
        TextView tv_text = languageDialog.findViewById(R.id.tv_text);

        tv_text.setText(getString(R.string.choose_your_language));
        btn_cancel.setText(getString(R.string.arabic));
        btn_sign_out.setText(getString(R.string.english));

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialog.dismiss();

                SharedPreferencesUtils.saveLanguage(getActivity(), "en");

                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
                getActivity().recreate();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialog.dismiss();

                SharedPreferencesUtils.saveLanguage(getActivity(), "ar");

                Locale locale = new Locale("ar");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
                getActivity().recreate();

            }
        });

        languageDialog.show();
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
    }
}
