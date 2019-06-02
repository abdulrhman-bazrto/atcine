package com.gnusl.actine.enums;

public enum AppCategories {

    Movies(0),
    TvShows(1),
    ;


    int type;

    AppCategories(int i) {
        this.type = i;
    }

    public int getType() {
        return type;
    }
}
