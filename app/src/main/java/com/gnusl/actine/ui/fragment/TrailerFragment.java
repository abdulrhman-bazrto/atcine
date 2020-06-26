package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.activity.TrailerActivity;
import com.gnusl.actine.util.Constants;
import com.squareup.picasso.Picasso;


public class TrailerFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    TextView tvShowTitle;
    ImageView ivShowImage, ivPlayShow;
    private Show show;
    Animation animation;
    private ConstraintLayout clRoot;

    public TrailerFragment() {
    }

    public static TrailerFragment newInstance(Bundle bundle) {
        TrailerFragment fragment = new TrailerFragment();


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
            inflatedView = inflater.inflate(R.layout.fragment_trailer, container, false);

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
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_right_side);

        tvShowTitle.setText(show.getTitle());
        Picasso.with(getActivity()).load(show.getCoverImageUrl()).into(ivShowImage);
    }

    private void findViews() {
        tvShowTitle = inflatedView.findViewById(R.id.tv_show_title);
        ivShowImage = inflatedView.findViewById(R.id.iv_movie_image);
        ivPlayShow = inflatedView.findViewById(R.id.iv_play_show);
        clRoot = inflatedView.findViewById(R.id.root_view);

        ivPlayShow.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_show:{
                Intent intent  = new Intent(getActivity(),TrailerActivity.class);
                intent.putExtra("id",show.getTrailerId());
                getActivity().startActivity(intent);
                break;
            }

        }
    }

    public void startAnimation() {
        animation.setDuration(1200);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        clRoot.startAnimation(animation);
    }
}
