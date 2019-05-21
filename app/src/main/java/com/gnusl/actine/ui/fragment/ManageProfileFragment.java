package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.ManageProfileAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;


public class ManageProfileFragment extends Fragment implements View.OnClickListener, ProfileClick {

    View inflatedView;

    private RecyclerView rvProfiles;
    private ManageProfileAdapter manageProfileAdapter;

    private ImageView ivAddProfile;

    private CustomAppBarWithBack cubManageProfile;

    public ManageProfileFragment() {
    }

    public static ManageProfileFragment newInstance() {
        ManageProfileFragment fragment = new ManageProfileFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_manage_profile, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        cubManageProfile = inflatedView.findViewById(R.id.cub_manage_profile_with_back);
        ivAddProfile = inflatedView.findViewById(R.id.iv_add_profile);

        ivAddProfile.setOnClickListener(this);

        cubManageProfile.getTvTitle().setText("Manage Profile");

        cubManageProfile.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        rvProfiles = inflatedView.findViewById(R.id.rv_profiles);

        manageProfileAdapter = new ManageProfileAdapter(getActivity(), this);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);

        rvProfiles.setLayoutManager(layoutManager);

        rvProfiles.setAdapter(manageProfileAdapter);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_profile: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onClickProfile() {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment);
            }
        }
    }

}
