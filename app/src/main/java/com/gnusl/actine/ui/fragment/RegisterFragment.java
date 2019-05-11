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
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.custom.CustomAppBarRegister;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private Button btnGotoStep2, btnGotoStep3, btnPayment;
    private View viewStep1, viewStep2, viewStep3;

    private TextView tvAlreadyUser;
    private CustomAppBarRegister cubRegister;


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_register, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();


        btnGotoStep2.setOnClickListener(this);
        btnGotoStep3.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        cubRegister.getBtnLogin().setOnClickListener(this);

        SpannableString ss = new SpannableString(tvAlreadyUser.getText().toString());
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() != null) {
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.LoginFragment);
                }
            }
        };
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        ss.setSpan(bss, tvAlreadyUser.getText().toString().lastIndexOf("?") + 2, tvAlreadyUser.getText().toString().length(), 0);
        ss.setSpan(span1, tvAlreadyUser.getText().toString().lastIndexOf("?") + 2, tvAlreadyUser.getText().toString().length(), 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), tvAlreadyUser.getText().toString().lastIndexOf("?") + 2, tvAlreadyUser.getText().toString().length(), 0);

        tvAlreadyUser.setMovementMethod(LinkMovementMethod.getInstance());
        tvAlreadyUser.setText(ss);

    }

    private void findViews() {
        viewStep1 = inflatedView.findViewById(R.id.sv_step1);
        viewStep2 = inflatedView.findViewById(R.id.ll_step2);
        viewStep3 = inflatedView.findViewById(R.id.cl_step3);

        btnGotoStep2 = inflatedView.findViewById(R.id.btn_goto_step2);
        btnGotoStep3 = inflatedView.findViewById(R.id.btn_register);
        btnPayment = inflatedView.findViewById(R.id.btn_start_membership);

        tvAlreadyUser = inflatedView.findViewById(R.id.tv_already_user);

        cubRegister = inflatedView.findViewById(R.id.cub_register);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_step2: {
                viewStep1.setVisibility(View.GONE);
                viewStep2.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.btn_register: {
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.btn_start_membership: {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                break;
            }
            case R.id.btn_login: {
                if (getActivity() != null) {
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.LoginFragment);
                }
                break;
            }
        }
    }

}
