package com.gnusl.actine.ui.Mobile.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.CategoryItem;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.Mobile.adapter.CategorySpinnerAdapter;
import com.gnusl.actine.ui.Mobile.adapter.EpisodeAdapter;
import com.gnusl.actine.ui.Mobile.custom.MarginItemDecoration;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EpisodesFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;
    Spinner spSeasons;

    private TextView tvEpisodes, tvSeasons;
    private Show show;

    RecyclerView rvEpisodes;

    EpisodeAdapter episodeAdapter;
    Animation animation;
    private ConstraintLayout clRoot;

    //    ArrayList<Cast> cast;
    public EpisodesFragment() {
    }

    public static EpisodesFragment newInstance(Bundle bundle) {
        EpisodesFragment fragment = new EpisodesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            show = (Show) getArguments().getSerializable(Constants.HomeDetailsExtra.getConst());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_episodes, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        findViews();
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_side);

//        initCategorySpinner();

//        tvDirector.setText(show.getDirector());
//        tvWriters.setText(show.getWriters());
//        tvReleaseDate.setText(show.getYear() + "");
//        tvType.setText(show.getCategory());
////        tvLanguage.setText(show.getLanguage());
//

        spSeasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Show> episodes = new ArrayList<>();
//                List<Show> episodes = show.getSeasons().get(position).getEpisodes();
                tvSeasons.setText((position + 1) + "/" + show.getSeasons().size());
                for (Show dbShow : show.getSeasons().get(position).getEpisodes()) {
                    File internalStorage = getActivity().getFilesDir();
                    File file = new File(internalStorage, dbShow.getTitle() + ".mp4");
                    if (file.exists()) {
                        dbShow.setInStorage(true);
                    } else {
                        dbShow.setInStorage(false);
                    }
                    episodes.add(dbShow);
                }

                tvEpisodes.setText(episodes.size() + " " + getActivity().getString(R.string.episodes));
                episodeAdapter.setList(episodes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                episodeAdapter.setList(show.getSeasons().get(0).getEpisodes());
            }
        });
        episodeAdapter = new EpisodeAdapter(getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvEpisodes.setLayoutManager(layoutManager);
        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getActivity().getResources().getDisplayMetrics());
        rvEpisodes.addItemDecoration(new MarginItemDecoration(20 * dp1, getActivity(), R.drawable.divider));
        rvEpisodes.setAdapter(episodeAdapter);
//        sendGetCastRequest();

    }

    private void findViews() {
        tvEpisodes = inflatedView.findViewById(R.id.tv_episodes);
        tvSeasons = inflatedView.findViewById(R.id.tv_seasons);
        rvEpisodes = inflatedView.findViewById(R.id.rv_episodes);
        clRoot = inflatedView.findViewById(R.id.root_view);

        spSeasons = inflatedView.findViewById(R.id.sp_seasons);

        Utils.setOnFocusScale(spSeasons);

    }

    private void initCategorySpinner() {
        final List<CategoryItem> list = new ArrayList<>();
        if (show.getSeasons() != null) {
            for (int i = 0; i < show.getSeasons().size(); i++) {
                CategoryItem season = new CategoryItem(getActivity().getString(R.string.season) + (i + 1));
                list.add(season);
            }
        }
        if (getActivity() != null) {
            CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getActivity(),
                    R.layout.item_spinner_season, list, R.layout.item_spinner_season);
            adapter.setDropDownViewResource(R.layout.item_spinner_season);
            spSeasons.setAdapter(adapter);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

//        if (jsonObject.has("data")) {
//            List<Cast> cast = Cast.newArray(jsonObject.optJSONArray("data"));
//            castAdapter.setList(cast);
//        }

    }

    public void setShow(Show show) {
        this.show = show;
        initCategorySpinner();
        spSeasons.setSelection(0);
    }

    public void startAnimation() {
        animation.setDuration(1200);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        clRoot.startAnimation(animation);
    }


}
