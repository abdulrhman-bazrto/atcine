package com.gnusl.actine.ui.fragment;

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
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MyListAdapter;
import com.gnusl.actine.ui.animate.ResizeAnimation;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.ui.custom.LoaderPopUp1;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.List;


public class MySeriesFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate {

    View inflatedView;
    private ConstraintLayout clRoot;

    private RecyclerView rvMyList;
    private MyListAdapter myListAdapter;
    LoaderPopUp1 loaderPopUp1 = new LoaderPopUp1();
    private Animation animation;

    public MySeriesFragment() {
    }

    public static MySeriesFragment newInstance() {
        MySeriesFragment fragment = new MySeriesFragment();
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
            init();
        } else {
            ViewGroup parent = (ViewGroup) inflatedView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }

        return inflatedView;
    }

    private void init() {
        clRoot = inflatedView.findViewById(R.id.root_view);

        rvMyList = inflatedView.findViewById(R.id.rv_my_list);
        myListAdapter = new MyListAdapter(getActivity(), this);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_side);

        GridLayoutManager layoutManager;
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            layoutManager = new GridLayoutManager(getActivity(), 5);
        } else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            layoutManager = new GridLayoutManager(getActivity(), 5);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        }

        rvMyList.setLayoutManager(layoutManager);

        rvMyList.setAdapter(myListAdapter);

        loaderPopUp1.show1(getActivity());

        DataLoader.getRequest(Urls.SeriesMyList.getLink(), this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onClickMovie(Show show, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
            }
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
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        loaderPopUp1.dismissLoader();

        List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
        myListAdapter.setList(series);

        if (series.isEmpty()) {
            inflatedView.findViewById(R.id.hint).setVisibility(View.VISIBLE);
        } else {
            inflatedView.findViewById(R.id.hint).setVisibility(View.GONE);
        }
    }

    public void refreshData() {
        myListAdapter = new MyListAdapter(getActivity(), this);
        rvMyList.setAdapter(myListAdapter);
        myListAdapter.notifyDataSetChanged();
        loaderPopUp1.show(getActivity());


        DataLoader.getRequest(Urls.SeriesMyList.getLink(), this);
    }

    public void startanimation() {
        animation.setDuration(1200);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        clRoot.startAnimation(animation);
    }
}
