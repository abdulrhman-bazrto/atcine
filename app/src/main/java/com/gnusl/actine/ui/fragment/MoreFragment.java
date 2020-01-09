package com.gnusl.actine.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.model.Profile;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AccountActivity;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.ProfilesAdapter;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MoreFragment extends Fragment implements View.OnClickListener, ProfileClick, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvProfiles;
    private ProfilesAdapter profilesAdapter;

    private Button btnManageProfile;

    private TextView tvMyList, tvHelp, tvLogout, tvAppSetting,tvAccount;

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

        DataLoader.getRequest(Urls.Profiles.getLink(), this);

        findViews();

        profilesAdapter = new ProfilesAdapter(getActivity(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvProfiles.setLayoutManager(layoutManager);

        rvProfiles.setAdapter(profilesAdapter);

    }

    private void findViews() {
        rvProfiles = inflatedView.findViewById(R.id.rv_profiles);
        btnManageProfile = inflatedView.findViewById(R.id.btn_manage_profile);
        tvMyList = inflatedView.findViewById(R.id.tv_my_list);
        tvHelp = inflatedView.findViewById(R.id.tv_help);
        tvLogout = inflatedView.findViewById(R.id.tv_logout);
        tvAppSetting = inflatedView.findViewById(R.id.tv_app_setting);
        tvAccount = inflatedView.findViewById(R.id.tv_account);

        btnManageProfile.setOnClickListener(this);
        tvMyList.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvAppSetting.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_manage_profile: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.ManageProfilesExtra.getConst(), (ArrayList<? extends Parcelable>) profilesAdapter.getList());
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ManageProfileFragment, bundle);
                    }
                }
                break;
            }
            case R.id.tv_my_list: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.MyListFragment, null);
                    }
                }
                break;
            }
            case R.id.tv_help: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.HelpFragment, null);
                    }
                }
                break;
            }
            case R.id.tv_app_setting: {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.AppSettingsFragment, null);
                    }
                }
                break;
            }
            case R.id.tv_account: {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                intent.putExtra("url",Urls.Account.getLink());
                startActivity(intent);
                break;
            } case R.id.tv_logout: {
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

        logoutDialog.findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
                DataLoader.postRequest(Urls.Logout.getLink(), new HashMap<>(), new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {
                        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        SharedPreferencesUtils.clear();
                        startActivity(new Intent(getActivity(), AuthActivity.class));
                        getActivity().finish();
                    }
                });
            }
        });

        logoutDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });

        logoutDialog.show();
    }

    @Override
    public void onClickProfile(Profile profile) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.EditNewProfileExtra.getConst(), profile);
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment, bundle);
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("profiles")) {
            List<Profile> profiles = Profile.newList(jsonObject.optJSONArray("profiles"));
            profilesAdapter.setList(profiles);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DataLoader.getRequest(Urls.Profiles.getLink(), this);
    }
}
