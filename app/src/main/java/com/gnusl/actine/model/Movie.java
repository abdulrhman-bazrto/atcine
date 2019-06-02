package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Show {

    private String preWatch;
    private String watchTime;
    private int year;

    public Movie(JSONObject jsonObject) {
        super(jsonObject);
    }


    public static Movie newInstance(JSONObject jsonObject) {
        Movie movie = new Movie(jsonObject);
        movie.setPreWatch(jsonObject.optString("pre_watch"));
        movie.setWatchTime(jsonObject.optString("watch_time"));
        movie.setYear(jsonObject.optInt("year"));

        return movie;

    }

    public static List<Movie> newList(JSONArray jsonArray) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            movies.add(newInstance(jsonArray.optJSONObject(i)));
        }
        return movies;
    }


    public String getPreWatch() {
        return preWatch;
    }

    public void setPreWatch(String preWatch) {
        this.preWatch = preWatch;
    }

    public String getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(String watchTime) {
        this.watchTime = watchTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
