package com.gnusl.actine.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.androidnetworking.error.ANError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MovieMoreLikeAdapter;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;


public class SearchResultFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate, LoadMoreDelegate {

    View inflatedView;

    ShimmerRecyclerView rvSearchResult;
    private MovieMoreLikeAdapter movieMoreLikeAdapter;

    private String searchType;
    private String searchFor;
    private String key;
    private String tvTransitionName;
    private TextView tvTitle;
    private String txtTitle;
    private String rvTransitionName;
    private ImageView ivBack;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(Bundle bundle) {
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.searchFor = getArguments().getString("searchFor");
            this.searchType = getArguments().getString("searchType");

            this.key = getArguments().getString("key");
            this.tvTransitionName = Objects.toString(getArguments().getString("transition"), "");
            this.rvTransitionName = Objects.toString(getArguments().getString("transition_rv"), "");
            this.txtTitle = Objects.toString(getArguments().getString("title"), "");

        }
//        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

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
        tvTitle.setText(txtTitle);

        movieMoreLikeAdapter = new MovieMoreLikeAdapter(getActivity(), this, this);
        GridLayoutManager gridLayoutManager;
        int count = 3;
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 5);
            count = 5;
        } else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 5);
            count = 5;
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            count = 4;
        }
        rvSearchResult.setLayoutManager(gridLayoutManager);

        rvSearchResult.setAdapter(movieMoreLikeAdapter);


//        rvSearchResult.showShimmerAdapter();


        String url = "";
        if (searchFor.equalsIgnoreCase("series"))
            url = Urls.Series.getLink();
        else if (searchFor.equalsIgnoreCase("movies"))
            url = Urls.Movies.getLink();

        url = url.substring(0, url.length() - 1);
        url += "?";
        switch (searchType) {
            case "category": {
                url += "category_id=" + key;
                break;
            }
            case "title": {
                url += "title=" + key;
                break;
            }
        }
        LoaderPopUp.show1(getActivity());


        DataLoader.getRequest(url + "&skip=" + 0 + "&take=" + 10, this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });
    }

    private void findViews() {
        rvSearchResult = inflatedView.findViewById(R.id.rv_search_result);
        tvTitle = inflatedView.findViewById(R.id.tv_title);
        tvTitle.setTransitionName(tvTransitionName);
        if (!rvTransitionName.isEmpty()) {
            rvSearchResult.setTransitionName(rvTransitionName);
        }
        ivBack = inflatedView.findViewById(R.id.iv_back);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onClickMovie(Show movie, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof SearchContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                ViewCompat.setTransitionName(ivThumbnail, "transition" + movie.getId());
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            }
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail, null, null);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof SearchContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
            }
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null, null, null);
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
        LoaderPopUp.dismissLoader();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();


//        if (!(rvSearchResult.getAdapter() instanceof MovieMoreLikeAdapter))
//            rvSearchResult.hideShimmerAdapter();

        if (jsonObject.has("series")) {
            List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
            movieMoreLikeAdapter.setList(series);
            if (series.size() == 0 && movieMoreLikeAdapter.getItemCount() == 0) {
                inflatedView.findViewById(R.id.tv_hint).setVisibility(View.VISIBLE);
            } else {
                inflatedView.findViewById(R.id.tv_hint).setVisibility(View.GONE);
            }
        }

        if (jsonObject.has("movies")) {
            List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
            movieMoreLikeAdapter.setList(movies);
            if (movies.size() == 0 && movieMoreLikeAdapter.getItemCount() == 0) {
                inflatedView.findViewById(R.id.tv_hint).setVisibility(View.VISIBLE);
            } else {
                inflatedView.findViewById(R.id.tv_hint).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void loadMore(int skip) {
        String url = "";
        if (searchFor.equalsIgnoreCase("series"))
            url = Urls.Series.getLink();
        else if (searchFor.equalsIgnoreCase("movies"))
            url = Urls.Movies.getLink();

        url = url.substring(0, url.length() - 1);
        url += "?";
        switch (searchType) {
            case "category": {
                url += "category_id=" + key;
                break;
            }
            case "title": {
                url += "title=" + key;
                break;
            }
        }

        DataLoader.getRequest(url + "&skip=" + skip + "&take=" + 10, this);
    }
}
