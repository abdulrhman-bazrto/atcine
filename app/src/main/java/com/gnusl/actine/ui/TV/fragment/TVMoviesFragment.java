package com.gnusl.actine.ui.TV.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreCategoriesDelegate;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.adapter.HomeAdapter;
import com.gnusl.actine.ui.Mobile.adapter.TVHomeAdapter;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.ui.Mobile.fragment.HomeContainerFragment;
import com.gnusl.actine.ui.TV.adapter.TVGenresAdapter;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.Helpers;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TVMoviesFragment extends Fragment implements HomeMovieClick, GenresClickEvents, ConnectionDelegate, LoadMoreCategoriesDelegate {

    View inflatedView;

    private RecyclerView rvHome, rvCategories;
    private TVHomeAdapter homeAdapter;

    private TVGenresAdapter genresAdapter;
    private List<Category> categories;

    public TVMoviesFragment() {
    }

    public static TVMoviesFragment newInstance() {
        TVMoviesFragment fragment = new TVMoviesFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setReenterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_tv_movies, container, false);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (homeAdapter == null || homeAdapter.getItemCount() == 0)
                    init(true);
            }
        }, 3000);

        return inflatedView;
    }

    private void init(boolean isFirstInit) {

        findViews();
        DataLoader.getRequest(Urls.MoviesGroups.getLink(), this);
        LoaderPopUp.show1(getActivity());

        if (isFirstInit) {
            genresAdapter = new TVGenresAdapter(getActivity(), new ArrayList<>(), TVMoviesFragment.this);

            CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            int max_visible_items = (int) Math.ceil(Helpers.convertPixelsToDp(size.x, getActivity()) / 500 * 2);
            layoutManager.setMaxVisibleItems(max_visible_items);
            rvCategories.setLayoutManager(layoutManager);

            rvCategories.setAdapter(genresAdapter);
        }

        rvHome.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(5);

        rvHome.setLayoutManager(layoutManager);

        homeAdapter = new TVHomeAdapter(getActivity(), rvHome, this, this, this);

        rvHome.setAdapter(homeAdapter);

    }

    private void findViews() {
        rvHome = inflatedView.findViewById(R.id.rv_home);
        rvCategories = inflatedView.findViewById(R.id.rv_categories);

    }

    @Override
    public void onClickMovie(Show movie, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
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
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null, null, null);
            }
        }
    }

    @Override
    public void onSelectGenres(Category genres, TextView tvListTitle, RecyclerView rvCategory) {
//        cubHome.getSpGenres().setText(genres.getTitle());
//        sgvHome.setVisibility(View.GONE);
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putString("searchFor", "movies");
                if (rvCategory != null) {
                    bundle.putString("transition_rv", "transition_rv" + genres.getId() + genres.getTitle());
                }
                bundle.putString("transition", "transition" + genres.getId() + genres.getTitle());
                bundle.putString("title", genres.getTitle());
                bundle.putString("searchType", "category");
                bundle.putString("key", String.valueOf(genres.getId()));
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.SearchResultFragment, bundle, null, rvCategory, tvListTitle);
            }
        }
    }

    @Override
    public void onCloseGenres() {

    }

    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.dismissLoader();
//        if (getActivity() != null)
//            // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("trend")) {

            if (genresAdapter != null) {
                categories = Category.newList(jsonObject.optJSONArray("movies_categories"));
                genresAdapter.setList(categories);

            }
            Show trendMovie = Show.newInstance(jsonObject.optJSONObject("trend"), true, false, false);

            List<Category> categories = Category.newList(jsonObject.optJSONArray("categories"), true);
            homeAdapter.setData(trendMovie, categories);

            LoaderPopUp.dismissLoader();
        }

    }

    @Override
    public void loadMoreCategories(int skip) {

    }

}
