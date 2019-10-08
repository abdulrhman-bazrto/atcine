package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.User;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;


public class LoginFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;

    private Button btnLogin;
    private EditText etEmailPhone, etPassword;
    private TextView tvSignUp;
    private KProgressHUD progressHUD;


    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_login, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        tvSignUp = inflatedView.findViewById(R.id.tv_sign_up);

        btnLogin = inflatedView.findViewById(R.id.btn_login);
        etEmailPhone = inflatedView.findViewById(R.id.et_email_phone);
        etPassword = inflatedView.findViewById(R.id.et_password);

        btnLogin.setOnClickListener(this);


        SpannableString ss = new SpannableString(tvSignUp.getText().toString());
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() != null) {
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.RegisterFragment);
                }
            }
        };
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        ss.setSpan(bss, tvSignUp.getText().toString().lastIndexOf("?") + 2, tvSignUp.getText().toString().length(), 0);
        ss.setSpan(span1, tvSignUp.getText().toString().lastIndexOf("?") + 2, tvSignUp.getText().toString().length(), 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), tvSignUp.getText().toString().lastIndexOf("?") + 2, tvSignUp.getText().toString().length(), 0);

        tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        tvSignUp.setText(ss);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                if (valid()) {
                    progressHUD = KProgressHUD.create(getActivity())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel(getString(R.string.please_wait))
                            .setMaxProgress(100)
                            .show();
                    sendLoginRequest();
                }
                break;
            }
        }
    }

    private boolean valid() {
        if (etEmailPhone.getText().toString().trim().isEmpty()) {
            etEmailPhone.setError(getString(R.string.hint_mobile_or_email));
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError(getString(R.string.hint_empty_password));
            return false;
        }
        return true;
    }

    private void sendLoginRequest() {
        HashMap<String, String> body = new HashMap<>();
        if (etEmailPhone.getText().toString().contains("@"))
            body.put("email_mobile", etEmailPhone.getText().toString());
        else
            body.put("email_mobile", etEmailPhone.getText().toString());
        body.put("password", etPassword.getText().toString());

        DataLoader.postRequest(Urls.Login.getLink(), body, this);

    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();
        if (jsonObject.has("user")) {
            SharedPreferencesUtils.saveUser(User.parse(jsonObject.optJSONObject("user")));
        }
        if (jsonObject.has("token")) {
            SharedPreferencesUtils.saveToken(jsonObject.optString("token"));
        }
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
}
