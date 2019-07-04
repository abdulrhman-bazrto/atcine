package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.activity.MainActivity;


public class SearchFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private EditText etSearch;
    private RadioButton rbSeries, rbMovies;

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

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (getActivity() != null) {
                        Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                        if (fragment instanceof SearchContainerFragment) {
                            Bundle bundle = new Bundle();
                            if (rbSeries.isChecked())
                                bundle.putString("searchFor", "series");
                            else if (rbMovies.isChecked())
                                bundle.putString("searchFor", "movies");
                            bundle.putString("searchType", "title");
                            bundle.putString("key", etSearch.getText().toString());
                            ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.SearchResultFragment, bundle);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void findViews() {
        etSearch = inflatedView.findViewById(R.id.et_search);
        rbMovies = inflatedView.findViewById(R.id.rb_movies);
        rbSeries = inflatedView.findViewById(R.id.rb_series);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

}
