package com.gnusl.actine.interfaces;

import android.widget.ImageView;

import com.gnusl.actine.model.Show;

public interface HomeMovieClick {

    void onClickMovie(Show movie, ImageView ivThumbnail);

    void onClickSeries(Show series);
}
