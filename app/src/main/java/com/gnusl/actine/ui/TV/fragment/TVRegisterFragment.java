package com.gnusl.actine.ui.TV.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.TV.activity.TVAuthActivity;
import com.gnusl.actine.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TVRegisterFragment extends Fragment {

    View inflatedView;
    Button btn_register;
    EditText et_name, et_username, et_password, et_password_confirm;

    public TVRegisterFragment() {
    }

    public static TVRegisterFragment newInstance() {
        TVRegisterFragment fragment = new TVRegisterFragment();
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
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_tvregister, container, false);
            init();
        }
        return inflatedView;
    }

    public void init() {

        findViews();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_username.getText().toString().isEmpty()) {
                    et_username.setError(getActivity().getString(R.string.cant_be_empty));
                    return;
                }
                Pattern p = Patterns.EMAIL_ADDRESS;
                Matcher m = p.matcher(et_username.getText().toString());
                if (!m.matches()) {
                    et_username.setError(getActivity().getString(R.string.invalid_email));
                    return;
                }
                if (et_password.getText().toString().isEmpty()) {
                    et_password.setError(getActivity().getString(R.string.cant_be_empty));
                    return;
                }
                if (et_password_confirm.getText().toString().isEmpty()) {
                    et_password_confirm.setError(getActivity().getString(R.string.cant_be_empty));
                    return;
                }
                if (!et_password_confirm.getText().toString().equalsIgnoreCase(et_password.getText().toString())) {
                    et_password_confirm.setError(getActivity().getString(R.string.mismatch));
                    return;
                }

                if (getActivity() != null && getActivity() instanceof TVAuthActivity){
                    Bundle bundle = new Bundle();
                    bundle.putString("name",et_name.getText().toString());
                    bundle.putString("username",et_username.getText().toString());
                    bundle.putString("password",et_password.getText().toString());
                    ((TVAuthActivity)getActivity()).openRegister1Fragment(bundle);
                }
            }
        });
    }

    private void findViews() {
        et_name = inflatedView.findViewById(R.id.et_name);
        et_username = inflatedView.findViewById(R.id.et_username);
        et_password = inflatedView.findViewById(R.id.et_password);
        et_password_confirm = inflatedView.findViewById(R.id.et_password_confirm);
        btn_register = inflatedView.findViewById(R.id.btn_register);

        Utils.setOnFocusScale(btn_register);

    }
}
