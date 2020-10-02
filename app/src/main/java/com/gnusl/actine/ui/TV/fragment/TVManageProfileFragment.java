package com.gnusl.actine.ui.TV.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.adapter.ProfilesAdapter;
import com.gnusl.actine.ui.Mobile.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.TV.activity.TVMainActivity;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.List;


public class TVManageProfileFragment extends Fragment implements View.OnClickListener, ProfileClick, ConnectionDelegate {

    View inflatedView;
    private RecyclerView rvProfiles;
    private ProfilesAdapter profilesAdapter;
    private ConstraintLayout clProfileDetails;

    public TVManageProfileFragment() {
    }

    public static TVManageProfileFragment newInstance() {
        TVManageProfileFragment fragment = new TVManageProfileFragment();
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
//        if (inflatedView == null) {
        inflatedView = inflater.inflate(R.layout.fragment_tvmanage_profile, container, false);
        init();
//        }
        return inflatedView;
    }

    private void init() {
        DataLoader.getRequest(Urls.Profiles.getLink(), this);

        findViews();

        profilesAdapter = new ProfilesAdapter(getActivity(), this);

        GridLayoutManager gridLayoutManager;

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rvProfiles.setLayoutManager(gridLayoutManager);

        rvProfiles.setAdapter(profilesAdapter);

    }

    private void findViews() {
        rvProfiles = inflatedView.findViewById(R.id.rv_profiles);
        clProfileDetails = inflatedView.findViewById(R.id.cl_profile_details);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionError(int code, String message) {

    }

    @Override
    public void onConnectionError(ANError anError) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("profiles")) {
            List<Profile> profiles = Profile.newList(jsonObject.optJSONArray("profiles"));
            profilesAdapter.setList(profiles);
        }
    }

    @Override
    public void onClickProfile(Profile profile, ImageView ivProfile, boolean isLongClick) {
        if (getActivity() != null) {
            if (isLongClick) {
                clProfileDetails.setVisibility(View.VISIBLE);
                TVEditNewProfileFragment fragment = (TVEditNewProfileFragment) getChildFragmentManager().findFragmentById(R.id.fragment_edit_profile);
                fragment.setProfile(profile,TVManageProfileFragment.this);
//                Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
//                if (fragment instanceof MoreContainerFragment) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(Constants.EditNewProfileExtra.getConst(), profile);
//                    bundle.putString("transition", ViewCompat.getTransitionName(ivProfile));
//                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.EditNewProfileFragment, bundle, ivProfile);
//                }
            }
//            else {
//                mySeriesFragment.refreshData();
//                myMoviesFragment.refreshData();
//            }
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//            ((TVMainActivity) getActivity()).changeSearchVisibility(isVisibleToUser);
//    }

    @Override
    public void onResume() {
        super.onResume();
        ((TVMainActivity) getActivity()).changeSearchVisibility(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((TVMainActivity) getActivity()).changeSearchVisibility(false);

    }

    public void hideEditFragment(boolean refresh) {
        clProfileDetails.setVisibility(View.GONE);
        if(refresh)
            init();
    }


}
