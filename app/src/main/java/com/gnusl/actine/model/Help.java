package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Help implements Serializable {

    private int id;
    private String title;
    private String description;
    private String iconUrl;

    public static Help newInstance(JSONObject jsonObject) {
        Help user = new Help();
        user.setId(jsonObject.optInt("id"));
        user.setTitle(jsonObject.optString("title"));
        user.setDescription(jsonObject.optString("description"));
        user.setIconUrl(jsonObject.optString("icon_url"));

        return user;
    }

    public static List<Help> newArray(JSONArray jsonArray) {
        List<Help> helpList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            helpList.add(newInstance(jsonArray.optJSONObject(i)));
        }
        return helpList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
