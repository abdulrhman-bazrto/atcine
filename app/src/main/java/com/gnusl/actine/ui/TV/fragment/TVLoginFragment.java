package com.gnusl.actine.ui.TV.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.User;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.AuthActivity;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.ui.Mobile.fragment.LoginFragment;
import com.gnusl.actine.ui.TV.activity.TVAuthActivity;
import com.gnusl.actine.ui.TV.activity.TVMainActivity;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;


public class TVLoginFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {
    View inflatedView;

    private Button btnLogin;
    private EditText etEmailPhone, etPassword;
    private TextView btn_language,  tvWelcome, tvForget, tvTest, tvTest1;
    private ImageView ivLogo;
    private ConstraintLayout clMain;

    public TVLoginFragment() {
    }

    public static TVLoginFragment newInstance() {
        TVLoginFragment fragment = new TVLoginFragment();
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
        inflatedView = inflater.inflate(R.layout.fragment_tvlogin, container, false);
        init();
//        }
        return inflatedView;
    }
    private void init() {
        findViews();
        btnLogin.setOnClickListener(this);
        Utils.setOnFocusScale(btnLogin);

    }
    private void findViews() {
        btnLogin = inflatedView.findViewById(R.id.btn_login);
        etEmailPhone = inflatedView.findViewById(R.id.et_email_phone);
        etPassword = inflatedView.findViewById(R.id.et_password);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
//                if (ivLogo.getVisibility() == View.GONE)
//                    ivLogo.setVisibility(View.VISIBLE);
//                else
//                    ivLogo.setVisibility(View.GONE);
                if (valid()) {
                    LoaderPopUp.show(getActivity());
                    sendLoginRequest(false);
                }
                break;
            }
        }
    }
    private boolean valid() {
        boolean tmp = true;
        if (etEmailPhone.getText().toString().trim().isEmpty()) {
            etEmailPhone.setError(getString(R.string.hint_mobile_or_email));
            tmp = false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError(getString(R.string.hint_empty_password));
            tmp = false;
        }
        return tmp;
    }
    private void sendLoginRequest(boolean withIgnore) {
        HashMap<String, String> body = new HashMap<>();
        if (etEmailPhone.getText().toString().contains("@"))
            body.put("email_mobile", etEmailPhone.getText().toString());
        else
            body.put("email_mobile", etEmailPhone.getText().toString());

        if (withIgnore)
            body.put("ignore", String.valueOf(true));

        body.put("password", etPassword.getText().toString());

        DataLoader.postRequest(Urls.Login.getLink(), body, this);

    }
    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();

        if (code == -10) {
            final Dialog confirmLoginDialog = new Dialog(getActivity());
            confirmLoginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (confirmLoginDialog.getWindow() != null)
                confirmLoginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmLoginDialog.setContentView(R.layout.dialog_another_login);
            confirmLoginDialog.setCancelable(true);

            TextView tvMsg = confirmLoginDialog.findViewById(R.id.tv_msg);

            tvMsg.setText(message);
            Utils.setOnFocusScale(confirmLoginDialog.findViewById(R.id.btn_sign_out));
            Utils.setOnFocusScale(confirmLoginDialog.findViewById(R.id.btn_cancel));
            confirmLoginDialog.findViewById(R.id.btn_sign_out).requestFocus();
            confirmLoginDialog.findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmLoginDialog.dismiss();
                    LoaderPopUp.show(getActivity());
                    sendLoginRequest(true);
                }
            });

            confirmLoginDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmLoginDialog.dismiss();
                }
            });

            confirmLoginDialog.show();

            return;
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.dismissLoader();
        Toast.makeText(getActivity(), R.string.error_happened, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();

        if (jsonObject.has("user")) {
            if (jsonObject.optJSONObject("user").optString("status").equalsIgnoreCase("paymentless")) {
                SharedPreferencesUtils.saveUser(User.parse(jsonObject.optJSONObject("user")));
                SharedPreferencesUtils.saveToken(jsonObject.optString("token"));
                SharedPreferencesUtils.saveCurrentSelectedPlan(jsonObject.optJSONObject("user").optString("user_type"));
                if (getActivity() != null) {

                    final Dialog confirmPaymentDialog = new Dialog(getActivity());
                    confirmPaymentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    if (confirmPaymentDialog.getWindow() != null)
                        confirmPaymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    confirmPaymentDialog.setContentView(R.layout.dialog_another_login);
                    confirmPaymentDialog.setCancelable(true);

                    TextView tvMsg = confirmPaymentDialog.findViewById(R.id.tv_msg);

                    tvMsg.setText(getActivity().getString(R.string.expired_account));

                    confirmPaymentDialog.findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmPaymentDialog.dismiss();
                            if (getActivity() != null)
                                ((AuthActivity) getActivity()).replaceFragment(FragmentTags.PaymentLessFragment);
                        }
                    });

                    confirmPaymentDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmPaymentDialog.dismiss();
                            if (getActivity() != null)
                                getActivity().onBackPressed();
                        }
                    });

                    confirmPaymentDialog.show();
                }
                return;
            } else if (jsonObject.optJSONObject("user").optString("status").equalsIgnoreCase("blocked")) {
                return;
            } else {
                SharedPreferencesUtils.saveUser(User.parse(jsonObject.optJSONObject("user")));
            }
        }
        if (jsonObject.has("token")) {
            SharedPreferencesUtils.saveToken(jsonObject.optString("token"));
        }
//        if (getActivity() != null) {
//            startActivity(new Intent(getActivity(), MainActivity.class));
//            getActivity().finish();
//        }
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), TVMainActivity.class));
            getActivity().finish();
//            ((TVAuthActivity) getActivity()).replaceFragment(FragmentTags.ToWatchFragment);
        }
    }
}
