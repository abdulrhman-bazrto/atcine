package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.CommentsAdapter;
import com.gnusl.actine.ui.adapter.MovieMoreLikeAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;

import java.util.ArrayList;


public class ShowDetailsFragment extends Fragment implements HomeMovieClick, View.OnClickListener {

    View inflatedView;

    private RecyclerView rvShowDetails;
    private CustomAppBarWithBack cubHomeWithBack;
    private Button btnReactions;
    private View clMoreLikeThis, clReactions;
    private RecyclerView rvComments;
    private CommentsAdapter commentsAdapter;

    public ShowDetailsFragment() {
    }

    public static ShowDetailsFragment newInstance() {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_show_details, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        btnReactions.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rvShowDetails.setLayoutManager(gridLayoutManager);

        MovieMoreLikeAdapter movieMoreLikeAdapter = new MovieMoreLikeAdapter(getActivity(), this);

        rvShowDetails.setAdapter(movieMoreLikeAdapter);


        commentsAdapter = new CommentsAdapter(getActivity(), new ArrayList<String>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvComments.setLayoutManager(layoutManager);

        rvComments.setAdapter(commentsAdapter);

    }

    private void findViews() {
        rvShowDetails = inflatedView.findViewById(R.id.rv_show_details);
        cubHomeWithBack = inflatedView.findViewById(R.id.cub_home_with_back);
        btnReactions = inflatedView.findViewById(R.id.btn_reactions);
        clMoreLikeThis = inflatedView.findViewById(R.id.cl_more_like_this);
        clReactions = inflatedView.findViewById(R.id.cl_reactions);
        rvComments = inflatedView.findViewById(R.id.rv_comments);

        cubHomeWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onClickMovie() {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment);
            } else if (fragment instanceof SearchContainerFragment) {
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reactions: {
                if (clMoreLikeThis.getVisibility() == View.VISIBLE) {
                    clMoreLikeThis.setVisibility(View.GONE);
                    clReactions.setVisibility(View.VISIBLE);
                } else {
                    clMoreLikeThis.setVisibility(View.VISIBLE);
                    clReactions.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
