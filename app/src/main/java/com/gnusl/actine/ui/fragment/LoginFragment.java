package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gnusl.actine.R;


public class LoginFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private Button loginButton;
    private EditText username, password;


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
        loginButton = inflatedView.findViewById(R.id.btn_login);
        username = inflatedView.findViewById(R.id.et_username);
        password = inflatedView.findViewById(R.id.et_password);

        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_login: {
//                ChatService.initiliazeService(getActivity());
//                break;
//            }
        }
    }

}
