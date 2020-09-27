package com.gnusl.actine.ui.TV.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.adapter.MyListAdapter;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp1;
import com.gnusl.actine.ui.Mobile.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.TV.activity.TVMainActivity;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.List;


public class TVMyMoviesFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate {

    View inflatedView;
    private RecyclerView rvMyList;
    private MyListAdapter myListAdapter;
    LoaderPopUp1 loaderPopUp1 = new LoaderPopUp1();

    public TVMyMoviesFragment() {
    }

    public static TVMyMoviesFragment newInstance() {
        TVMyMoviesFragment fragment = new TVMyMoviesFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_my_list, container, false);
//            init();
        } else {
            ViewGroup parent = (ViewGroup) inflatedView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }

        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        rvMyList = inflatedView.findViewById(R.id.rv_my_list);

        myListAdapter = new MyListAdapter(getActivity(), this, "TV");

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        rvMyList.setLayoutManager(layoutManager);
        rvMyList.setAdapter(myListAdapter);

        loaderPopUp1.show1(getActivity());

        DataLoader.getRequest(Urls.MoviesMyList.getLink(), this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onClickMovie(Show show, ImageView ivThumbnail) {
        if (getActivity() != null) {
//            Fragment fragment = ((TVMainActivity) getActivity()).getmCurrentFragment();
//            if (fragment instanceof MoreContainerFragment) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
//                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
//                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
//            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
//            Fragment fragment = ((TVMainActivity) getActivity()).getmCurrentFragment();
//            if (fragment instanceof MoreContainerFragment) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
//                bundle.putString("type", "season");
//                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
//            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        loaderPopUp1.dismissLoader();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        loaderPopUp1.dismissLoader();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        loaderPopUp1.dismissLoader();
        List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
        myListAdapter.setList(movies);
        if (movies.isEmpty()) {
            inflatedView.findViewById(R.id.hint).setVisibility(View.VISIBLE);
        } else {
            inflatedView.findViewById(R.id.hint).setVisibility(View.GONE);
        }

    }
}
