package com.gnusl.actine.interfaces;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.model.Category;

public interface GenresClickEvents {

    void onSelectGenres(Category genres, TextView tvListTitle, RecyclerView rvCategory);

    void onCloseGenres();
}
