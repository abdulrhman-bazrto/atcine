package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;


public class EditNewProfileFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private CustomAppBarWithBack cubManageProfile;

    private Button btnCancel;

    public EditNewProfileFragment() {
    }

    public static EditNewProfileFragment newInstance() {
        EditNewProfileFragment fragment = new EditNewProfileFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_edit_new_profile, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        cubManageProfile = inflatedView.findViewById(R.id.cub_manage_profile_with_back);
        btnCancel = inflatedView.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(this);

        cubManageProfile.getTvTitle().setText("Manage Profile");

        cubManageProfile.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel: {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        }
    }

}
