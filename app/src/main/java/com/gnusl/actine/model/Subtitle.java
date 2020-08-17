package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subtitle implements Serializable {


    private String label, path;


    public static Subtitle newInstance(JSONObject jsonObject) {
        Subtitle subtitle = new Subtitle();
        subtitle.setLabel(jsonObject.optString("label"));
        subtitle.setPath(jsonObject.optString("path"));
        return subtitle;
    }

    public static List<Subtitle> newList(JSONArray jsonArray) {
        List<Subtitle> profiles = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            profiles.add(newInstance(jsonArray.optJSONObject(i)));

        return profiles;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
