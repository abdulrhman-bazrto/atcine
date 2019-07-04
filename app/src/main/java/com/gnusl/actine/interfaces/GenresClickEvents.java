package com.gnusl.actine.interfaces;

import com.gnusl.actine.model.Category;

public interface GenresClickEvents {

    void onSelectGenres(Category genres);

    void onCloseGenres();
}
