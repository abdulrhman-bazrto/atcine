package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cast implements Serializable {

    private String name;
    private String type;

    public static Cast newInstance(JSONObject jsonObject) {
        Cast cast = new Cast();
        cast.setName(jsonObject.optString("name"));
        cast.setType(jsonObject.optString("type"));

        return cast;
    }

    public static List<Cast> newArray(JSONArray jsonArray) {
        List<Cast> casts = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            casts.add(newInstance(jsonArray.optJSONObject(i)));
        }
        return casts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
