package com.gnusl.actine.ui.Mobile.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
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
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.adapter.MyListAdapter;
import com.gnusl.actine.ui.Mobile.custom.CustomAppBarWithBack;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;


public class SeriesSeasonsFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate {

    View inflatedView;

    private CustomAppBarWithBack cubMyListWithBack;
    private RecyclerView rvMyList;
    private MyListAdapter myListAdapter;
    private Show show;
    private String type;
    private String seasonId;

    public SeriesSeasonsFragment() {
    }

    public static SeriesSeasonsFragment newInstance(Bundle bundle) {
        SeriesSeasonsFragment fragment = new SeriesSeasonsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.show = (Show) getArguments().getSerializable(Constants.HomeDetailsExtra.getConst());
            this.type = getArguments().getString("type");
            this.seasonId = getArguments().getString("seasonId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_series_seasons, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        cubMyListWithBack = inflatedView.findViewById(R.id.cub_my_list_with_back);
        rvMyList = inflatedView.findViewById(R.id.rv_series_season);


        cubMyListWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        myListAdapter = new MyListAdapter(getActivity(), this,"Mobile");
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


        if (type.equalsIgnoreCase("episode")) {
            cubMyListWithBack.getTvTitle().setText(getActivity().getString(R.string.episodes));
            myListAdapter.setList(show.getEpisodes());
        } else if (type.equalsIgnoreCase("season")) {
            cubMyListWithBack.getTvTitle().setText(getActivity().getString(R.string.seasons));
            LoaderPopUp.show(getActivity());

            DataLoader.getRequest(Urls.Series.getLink() + show.getId(), this);
        }
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
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
            bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));

            if (fragment instanceof HomeContainerFragment) {
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail, null, null);
            } else if (fragment instanceof MoreContainerFragment) {
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            } else if (fragment instanceof SearchContainerFragment) {
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, null, null, null);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null, null, null);
                }
            } else if (fragment instanceof MoreContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, null);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
                }
            } else if (fragment instanceof SearchContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, null);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
                }
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.show(getActivity());
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
//        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();

        if (jsonObject.has("series")) {
            if (jsonObject.optJSONObject("series") != null && jsonObject.optJSONObject("series").has("seasons")) {
                show = Show.newInstance(jsonObject.optJSONObject("series"), show.getIsMovie(), show.getIsSeason(), show.getIsEpisode());
//                show.setSeasons(jsonObject.optJSONObject("series").optJSONArray("seasons"));
                if (!show.getIsMovie())
                    myListAdapter.setList(show.getSeasons());
            }
        }
    }
}
