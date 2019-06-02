package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Serie extends Show {

    public Serie(JSONObject jsonObject) {
        super(jsonObject);
    }


    public static Serie newInstance(JSONObject jsonObject) {
        return new Serie(jsonObject);
    }

    public static List<Serie> newList(JSONArray jsonArray) {
        List<Serie> movies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            movies.add(newInstance(jsonArray.optJSONObject(i)));
        }
        return movies;
    }

}
