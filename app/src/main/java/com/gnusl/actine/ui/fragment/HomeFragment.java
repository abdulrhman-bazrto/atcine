package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.HomeAdapter;


public class HomeFragment extends Fragment implements HomeMovieClick {

    View inflatedView;

    private RecyclerView rvHome;
    private HomeAdapter homeAdapter;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
            init();
        }

        if (getActivity() != null) {
            ((MainActivity) getActivity()).showHomeAppBar();
        }

        return inflatedView;
    }

    private void init() {

        findViews();

        rvHome.setNestedScrollingEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(3);

        rvHome.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(getActivity(), this);

        rvHome.setAdapter(homeAdapter);

    }

    private void findViews() {
        rvHome = inflatedView.findViewById(R.id.rv_home);

    }

    @Override
    public void onClickMovie() {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment);
            }
        }
    }
}
