package com.gnusl.actine.ui.Mobile.fragment;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.adapter.GenresAdapter;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CategoriesFragment extends Fragment implements View.OnClickListener, ConnectionDelegate, GenresClickEvents {

    private View inflatedView;
    private RecyclerView rvCategories;
    private GenresAdapter genresAdapter;
    private ImageView ivBack;
    List<Category> categories= new ArrayList<>();

    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance(Bundle bundle) {
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            String json = getArguments().getString("categories",null);
            if (json == null || json.isEmpty()) {
                return ;
            } else {
                Type type = (new TypeToken<List<Category>>() {
                }).getType();
                categories= (List<Category>) gson.fromJson(json, type);
            }
        }
//        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_categories, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        genresAdapter = new GenresAdapter(getActivity(), categories, CategoriesFragment.this);

        GridLayoutManager gridLayoutManager;
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }


        rvCategories.setLayoutManager(gridLayoutManager);

        rvCategories.setAdapter(genresAdapter);
//        rvCategories.scheduleLayoutAnimation();

    }

    private void findViews() {
        rvCategories = inflatedView.findViewById(R.id.rv_search_result);
        ivBack = inflatedView.findViewById(R.id.iv_back);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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
                switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())) {
                    case TvShows: {
                        bundle.putString("searchFor", "series");
                        break;
                    }
                    case Movies: {
                        bundle.putString("searchFor", "movies");
                        break;
                    }
                }
                if (tvListTitle != null){
                    bundle.putString("transition", "transition" + genres.getId());
                    bundle.putString("title", genres.getTitle());
                }
                bundle.putString("searchType", "category");
                bundle.putString("key", String.valueOf(genres.getId()));
                bundle.putString("title", genres.getTitle());
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.SearchResultFragment, bundle, null, null, tvListTitle);
            }
        }
    }

    @Override
    public void onCloseGenres() {

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
        if (jsonObject.has("categories") && !jsonObject.has("trend")) {
            if (genresAdapter != null) {
                List<Category> categories = Category.newList(jsonObject.optJSONArray("categories"));
                genresAdapter.setList(categories);
            }
        }
    }
}
