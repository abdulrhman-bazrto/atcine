package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Cast;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.adapter.CastAdapter;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OverviewFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;

    private TextView tvDirector, tvWriters, tvReleaseDate, tvType, tvLanguage;
    private Show show;
    RecyclerView rvCast;
    CastAdapter castAdapter;
    ArrayList<Cast> cast;

    public OverviewFragment() {
    }

    public static OverviewFragment newInstance(Bundle bundle) {
        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            show = (Show) getArguments().getSerializable(Constants.HomeDetailsExtra.getConst());
//            cast = getArguments().getParcelableArrayList("crew");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_overview, container, false);
            init();
        }else {
            ViewGroup parent = (ViewGroup) inflatedView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
        return inflatedView;
    }

    private void init() {
        findViews();
//        tvDirector.setText(show.getDirector());
//        tvWriters.setText(show.getWriters());
        tvReleaseDate.setText(show.getYear() + "");
        tvType.setText(show.getCategory());
//        tvLanguage.setText(show.getLanguage());

        castAdapter = new CastAdapter(getActivity(), new ArrayList<Cast>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvCast.setLayoutManager(layoutManager);

        rvCast.setAdapter(castAdapter);

//        String url = "";
//        if (show.getIsMovie()) {
//            url = Urls.Movie.getLink();
//        } else  {
//            url = Urls.Series.getLink();
//        }
//        DataLoader.getRequest(url + show.getId(), this);

//        sendGetCastRequest();

    }

    private void findViews() {
        tvDirector = inflatedView.findViewById(R.id.tv_director);
        tvWriters = inflatedView.findViewById(R.id.tv_writers);
        tvReleaseDate = inflatedView.findViewById(R.id.tv_release);
        tvType = inflatedView.findViewById(R.id.tv_type);
        tvLanguage = inflatedView.findViewById(R.id.tv_language);
        rvCast = inflatedView.findViewById(R.id.rv_cast);

    }

    private void sendGetCastRequest() {

        if (show.getIsMovie()) {
            DataLoader.getRequest(Urls.MovieCast.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        } else if (show.getIsEpisode()) {
            DataLoader.getRequest(Urls.EpisodeCast.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
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
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

//        if (jsonObject.has("data")) {
//            List<Cast> cast = Cast.newArray(jsonObject.optJSONArray("data"));
//            castAdapter.setList(cast);
//        }
//        if (jsonObject.has("crew")) {
//            cast = (ArrayList<Cast>) Cast.newArray(jsonObject.optJSONArray("crew"));
//            castAdapter.setList(cast);
//        }
    }

    //
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (castAdapter != null && cast != null)
                castAdapter.setList(cast);
        } else {
        }
    }

    public void setCastList(ArrayList<Cast> cast1) {
        this.cast = cast1;
    }

}
