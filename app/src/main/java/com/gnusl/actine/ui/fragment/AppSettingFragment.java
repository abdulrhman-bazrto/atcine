package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;


public class AppSettingFragment extends Fragment implements View.OnClickListener {

    View inflatedView;


    public AppSettingFragment() {
    }

    public static AppSettingFragment newInstance() {
        AppSettingFragment fragment = new AppSettingFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_app_settings, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

}
