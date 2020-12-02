package com.gnusl.actine.ui.Mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.gnusl.actine.model.Cast;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.Mobile.adapter.CastAdapter;
import com.gnusl.actine.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;


public class OverviewFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;

    private TextView tvDirector, tvWriters, tvReleaseDate, tvType, tvLanguage, tvCast;
    private Show show;
    RecyclerView rvCast;
    CastAdapter castAdapter;
    ArrayList<Cast> cast;
    Animation animation;
    private String device;
    private ConstraintLayout clRoot, clMain;

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
            device = getArguments().getString("Device");
//            cast = getArguments().getParcelableArrayList("crew");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_overview, container, false);
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
        findViews();
        if (show.getIsMovie())
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_side);
        else
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_right_side);

        if (device.equalsIgnoreCase("TV")) {
            clMain.setVisibility(View.GONE);
            tvCast.setVisibility(View.GONE);
        }
//        tvDirector.setText(show.getDirector());
//        tvWriters.setText(show.getWriters());
        if (show.getYear() != 0)
            tvReleaseDate.setText(show.getYear() + "");
        else
            tvReleaseDate.setText("");
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
        clRoot = inflatedView.findViewById(R.id.root_view);
        clMain = inflatedView.findViewById(R.id.cl_main);
        tvCast = inflatedView.findViewById(R.id.tv_cast);
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
//        if (jsonObject.has("crew")) {
//            cast = (ArrayList<Cast>) Cast.newArray(jsonObject.optJSONArray("crew"));
//            castAdapter.setList(cast);
//        }
    }

    //
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (castAdapter != null && cast != null)
//                castAdapter.setList(cast);
//        } else {
//        }
//    }

    public void setCastList(ArrayList<Cast> cast1) {
        this.cast = cast1;
        if (castAdapter != null && cast != null) {
            castAdapter.setList(cast);
            if (cast.isEmpty()) {
                tvCast.setVisibility(View.GONE);
                rvCast.setVisibility(View.GONE);
            }
        } else {
            tvCast.setVisibility(View.GONE);
            rvCast.setVisibility(View.GONE);
        }
    }

    public void startAnimation() {
        animation.setDuration(1200);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        clRoot.startAnimation(animation);
    }
}
