package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String title;
    private int id;
    private List<Show> shows;

    public Category(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Category() {

    }

    public static Category newInstance(JSONObject jsonObject) {
        Category category = new Category();
        category.setId(jsonObject.optInt("id"));
        category.setTitle(jsonObject.optString("title"));
        return category;
    }

    public static Category newInstance(JSONObject jsonObject, boolean isMovie) {
        Category category = new Category();
        category.setId(jsonObject.optInt("category_id"));
        category.setTitle(jsonObject.optString("title"));
        category.setShows(Show.newList(jsonObject.optJSONArray("items"), isMovie, false, false));

        return category;
    }

    public static List<Category> newList(JSONArray jsonArray) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            categories.add(newInstance(jsonArray.optJSONObject(i)));
        }
        return categories;
    }

    public static List<Category> newList(JSONArray jsonArray, boolean isMovie) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            categories.add(newInstance(jsonArray.optJSONObject(i), isMovie));
        }
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}
