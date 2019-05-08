package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;


public class LoginFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private Button loginButton;
    private EditText username, password;
    private TextView tvSignUp;


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

        loginButton = inflatedView.findViewById(R.id.btn_login);
        username = inflatedView.findViewById(R.id.et_username);
        password = inflatedView.findViewById(R.id.et_password);

        loginButton.setOnClickListener(this);


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
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                break;
            }
        }
    }

}
