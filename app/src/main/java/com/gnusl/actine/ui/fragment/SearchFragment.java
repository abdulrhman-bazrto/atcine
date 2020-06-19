package com.gnusl.actine.ui.fragment;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreDelegate;
import com.gnusl.actine.model.CategoryItem;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.CategorySpinnerAdapter;
import com.gnusl.actine.ui.adapter.MovieMoreLikeAdapter;
import com.gnusl.actine.util.Constants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate, LoadMoreDelegate {

    View inflatedView;
    Spinner spCategory;
    TextView tvTitle;

    private EditText etSearch;
    private ConstraintLayout clMain, clMain1, clRoot;
    boolean toTop = true;
    private KProgressHUD progressHUD;

    private String searchType;
    private String searchFor;
    private String key;
    RecyclerView rvSearchResult;
    private MovieMoreLikeAdapter movieMoreLikeAdapter;
    private ConstraintLayout rootView;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_search, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        initCategorySpinner();
        spCategory.setSelection(0);

        movieMoreLikeAdapter = new MovieMoreLikeAdapter(getActivity(), this, this);
        GridLayoutManager gridLayoutManager;
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        } else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        }


        rvSearchResult.setLayoutManager(gridLayoutManager);

        rvSearchResult.setAdapter(movieMoreLikeAdapter);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (getActivity() != null) {


                        int modifierY;
                        if (toTop) {

                            int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                                    getActivity().getResources().getDisplayMetrics());
                            modifierY = tvTitle.getBottom() - clMain.getTop() + 30 * dp1;
                            Animation translateAnimation = new TranslateAnimation(0, 0, 0, modifierY);
                            translateAnimation.setDuration(1000);
                            translateAnimation.setFillEnabled(true);
                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    int[] pos = {clMain.getLeft(), clMain.getTop() + modifierY, clMain.getRight(), clMain.getBottom() + modifierY};
                                    clMain.layout(pos[0], pos[1], pos[2], pos[3]);
                                    ConstraintSet constraintSet = new ConstraintSet();
                                    constraintSet.clone(clRoot);
                                    constraintSet.clear(R.id.cl_main, ConstraintSet.BOTTOM);

                                    constraintSet.connect(R.id.cl_main, ConstraintSet.TOP,R.id.tv_title,ConstraintSet.BOTTOM,dp1*30);
                                    constraintSet.applyTo(clRoot);
                                    clMain1.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            clMain.startAnimation(translateAnimation);
                            toTop = !toTop;
                        }

                        if (spCategory.getSelectedItemPosition() == 0) {
                            searchFor = "movies";

                        } else {
                            searchFor = "series";
                        }
                        searchType = "title";
                        key = etSearch.getText().toString();
                        search();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void findViews() {
        etSearch = inflatedView.findViewById(R.id.et_search);
        spCategory = inflatedView.findViewById(R.id.sp_category);
        clMain = inflatedView.findViewById(R.id.cl_main);
        clMain1 = inflatedView.findViewById(R.id.cl_main1);
        tvTitle = inflatedView.findViewById(R.id.tv_title);
        rvSearchResult = inflatedView.findViewById(R.id.rv_search_result);
        clRoot = inflatedView.findViewById(R.id.root_view);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    private void search() {
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
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

movieMoreLikeAdapter.clearList();
        DataLoader.getRequest(url + "&skip=" + 0 + "&take=" + 20, this);
    }

    private void initCategorySpinner() {
        final List<CategoryItem> list = new ArrayList<>();
        CategoryItem movies = new CategoryItem("Movies");
        CategoryItem tvShows = new CategoryItem("TV Shows");
        list.add(movies);
        list.add(tvShows);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getActivity(),
                R.layout.item_spinner_category2, list,R.layout.item_spinner_category1);
        adapter.setDropDownViewResource(R.layout.item_spinner_category1);
        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getActivity().getResources().getDisplayMetrics());
        Display display = getActivity(). getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int _width = size.x ;

        spCategory.setDropDownWidth(_width);
        spCategory.setAdapter(adapter);
    }

    @Override
    public void onClickMovie(Show movie, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof SearchContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            }
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
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
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        if (progressHUD != null)
            progressHUD.dismiss();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();

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

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        rootView=inflatedView.findViewById(R.id.root_view);
//        LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation1);
//        rootView.setLayoutAnimation(anim);
//    }
}
