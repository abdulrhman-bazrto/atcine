package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.DBShow;
import com.gnusl.actine.model.DBShow_;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.DownloadsListAdapter;
import com.gnusl.actine.ui.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.custom.CustomAppBar;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.util.ObjectBox;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;


public class DownloadShowsFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    private View inflatedView;
    private RecyclerView rvDownloads;
    private TextView tvHint;
    private DownloadsListAdapter downloadsListAdapter;
    private AppCategories currentCategory = AppCategories.Movies;
    private String showType;
    private Button btnFindDownload;
    private View clEmptyDownloadList;

    public DownloadShowsFragment() {
    }

    public static DownloadShowsFragment newInstance() {
        DownloadShowsFragment fragment = new DownloadShowsFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_download_shows, container, false);
            init();
        } else {
//            ViewGroup parent = (ViewGroup) inflatedView.getParent();
//            if (parent != null) {
//                parent.removeAllViews();
//            }
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        btnFindDownload.setOnClickListener(this);

        downloadsListAdapter = new DownloadsListAdapter(getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        rvDownloads.setLayoutManager(gridLayoutManager);

        rvDownloads.setAdapter(downloadsListAdapter);

        LoaderPopUp.show(getActivity());

        Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
        List<DBShow> dbShows;
        if (showType.equalsIgnoreCase("series")) {
            dbShows = dbShowBox.query().equal(DBShow_.isMovie, false).build().find();
//            DataLoader.getRequest(Urls.SeriesMyList.getLink(), this);
            tvHint.setText("You don't have any series downloaded yet");
        } else {
            dbShows = dbShowBox.query().equal(DBShow_.isMovie, true).build().find();
//            DataLoader.getRequest(Urls.MoviesMyList.getLink(), this);
            tvHint.setText("You don't have any movies downloaded yet");

        }

        if (dbShows.size() != 0) {
            clEmptyDownloadList.setVisibility(View.GONE);

            List<Show> movies = new ArrayList<>();
            for (DBShow dbShow : dbShows) {
                Show show = dbShow.getShowObject();
                File internalStorage = getActivity().getFilesDir();
                File file = new File(internalStorage, show.getTitle() + ".mp4");
                if (file.exists())
                    show.setInStorage(true);
                else
                    show.setInStorage(false);
                movies.add(show);
            }
            downloadsListAdapter.setList(movies);
        } else {
            clEmptyDownloadList.setVisibility(View.VISIBLE);
        }

        LoaderPopUp.dismissLoader();

    }

    private void findViews() {
        rvDownloads = inflatedView.findViewById(R.id.rv_downloads);
        tvHint = inflatedView.findViewById(R.id.hint);
        btnFindDownload = inflatedView.findViewById(R.id.btn_find_download);
        clEmptyDownloadList = inflatedView.findViewById(R.id.cl_empty_downloads_hint);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_download: {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).replaceFragment(1);
                    return;
                }
                clEmptyDownloadList.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if (clEmptyDownloadList == null)
//                return;
            if (downloadsListAdapter == null)
                return;
            Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
            List<DBShow> dbShows;
            if (showType.equalsIgnoreCase("series"))
                dbShows = dbShowBox.query().equal(DBShow_.isMovie, false).build().find();
            else
                dbShows = dbShowBox.query().equal(DBShow_.isMovie, true).build().find();

            if (dbShows.size() != 0) {
                clEmptyDownloadList.setVisibility(View.GONE);
                List<Show> movies = new ArrayList<>();
                for (DBShow dbShow : dbShows) {
                    Show show = dbShow.getShowObject();
                    File internalStorage = getActivity().getFilesDir();
                    File file = new File(internalStorage, show.getTitle() + ".mp4");
                    if (file.exists())
                        show.setInStorage(true);
                    else
                        show.setInStorage(false);
                    movies.add(show);
                }
                downloadsListAdapter.setList(movies);
            } else {
                clEmptyDownloadList.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();
        if (code == 401) {
            SharedPreferencesUtils.clear();
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        }
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

        if (jsonObject.has("movies")) {
            clEmptyDownloadList.setVisibility(View.GONE);
//            clDownloadList.setVisibility(View.VISIBLE);

            List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
            File internalStorage = getActivity().getFilesDir();
            for (Show movie : movies) {
                File file = new File(internalStorage, movie.getTitle() + ".mp4");
//                if (file.exists())
                    movie.setInStorage(true);
//                else
//                    movie.setInStorage(false);
            }
            downloadsListAdapter.setList(movies);

        } else if (jsonObject.has("series")) {
            clEmptyDownloadList.setVisibility(View.GONE);

            List<Show> movies = Show.newList(jsonObject.optJSONArray("series"), false, true, true);
            File internalStorage = getActivity().getFilesDir();
            for (Show movie : movies) {
                File file = new File(internalStorage, movie.getTitle() + ".mp4");
//                if (file.exists())
                    movie.setInStorage(true);
//                else
//                    movie.setInStorage(false);
            }
            downloadsListAdapter.setList(movies);
        }

    }

    public void setShowType(String showType) {
        this.showType = showType;
    }
}
