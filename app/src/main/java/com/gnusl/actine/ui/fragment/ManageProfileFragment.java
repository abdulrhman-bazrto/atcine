package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.model.Profile;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.ManageProfileAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ManageProfileFragment extends Fragment implements View.OnClickListener, ProfileClick, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvProfiles;
    private ManageProfileAdapter manageProfileAdapter;
    private ImageView ivAddProfile;
    private CustomAppBarWithBack cubManageProfile;

    private List<Profile> profiles = new ArrayList<>();
    boolean requestOnBack = false;

    public ManageProfileFragment() {
    }

    public static ManageProfileFragment newInstance(Bundle bundle) {
        ManageProfileFragment fragment = new ManageProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.profiles = getArguments().getParcelableArrayList(Constants.ManageProfilesExtra.getConst());
        }
//        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
//        setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
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

        manageProfileAdapter = new ManageProfileAdapter(getActivity(), profiles, this);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);

        rvProfiles.setLayoutManager(layoutManager);

        rvProfiles.setAdapter(manageProfileAdapter);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_profile: {
                if (getActivity() != null) {
                    requestOnBack = true;
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof MoreContainerFragment) {
                        ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment, null, null);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onClickProfile(Profile profile, ImageView ivProfile,boolean isLongClick) {
        if (getActivity() != null) {
            requestOnBack = true;
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.EditNewProfileExtra.getConst(), profile);
                bundle.putString("transition", ViewCompat.getTransitionName(ivProfile));
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment, bundle,ivProfile);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requestOnBack)
            DataLoader.getRequest(Urls.Profiles.getLink(), this);
    }

    @Override
    public void onConnectionError(int code, String message) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        if (getActivity() != null)
            Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("profiles")) {
            List<Profile> profiles = Profile.newList(jsonObject.optJSONArray("profiles"));
            manageProfileAdapter.setList(profiles);
        }
    }
}
