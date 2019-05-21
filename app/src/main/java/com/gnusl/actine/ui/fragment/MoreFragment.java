package com.gnusl.actine.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.ProfilesAdapter;


public class MoreFragment extends Fragment implements View.OnClickListener, ProfileClick {

    View inflatedView;

    private RecyclerView rvProfiles;
    private ProfilesAdapter profilesAdapter;

    private Button btnManageProfile;

    private TextView tvMyList, tvHelp, tvLogout;

    public MoreFragment() {
    }

    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_more, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        rvProfiles = inflatedView.findViewById(R.id.rv_profiles);
        btnManageProfile = inflatedView.findViewById(R.id.btn_manage_profile);
        tvMyList = inflatedView.findViewById(R.id.tv_my_list);
        tvHelp = inflatedView.findViewById(R.id.tv_help);
        tvLogout = inflatedView.findViewById(R.id.tv_logout);

        btnManageProfile.setOnClickListener(this);
        tvMyList.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        profilesAdapter = new ProfilesAdapter(getActivity(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvProfiles.setLayoutManager(layoutManager);

        rvProfiles.setAdapter(profilesAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_manage_profile: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ManageProfileFragment);
                    }
                }
                break;
            }
            case R.id.tv_my_list: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.MyListFragment);
                    }
                }
                break;
            }
            case R.id.tv_help: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.HelpFragment);
                    }
                }
                break;
            }
            case R.id.tv_logout: {
               showLogoutDialog();
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
        logoutDialog.show();
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
