package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MovieMoreLikeAdapter;


public class SearchResultFragment extends Fragment implements View.OnClickListener, HomeMovieClick {

    View inflatedView;

    RecyclerView rvSearchResult;
    private MovieMoreLikeAdapter movieMoreLikeAdapter;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance() {
        SearchResultFragment fragment = new SearchResultFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_search_result, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        movieMoreLikeAdapter = new MovieMoreLikeAdapter(getActivity(), this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rvSearchResult.setLayoutManager(gridLayoutManager);

        rvSearchResult.setAdapter(movieMoreLikeAdapter);


    }

    private void findViews() {
        rvSearchResult = inflatedView.findViewById(R.id.rv_search_result);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onClickMovie() {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof SearchContainerFragment) {
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment);
            }
        }
    }
}
