package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String title, count, imageURL,type;
    private int id;
    private List<Show> shows;

    public Category(String title, String count, String imageURL, String type, int id) {
        this.title = title;
        this.count = count;
        this.imageURL = imageURL;
        this.type = type;
        this.id = id;
    }

    public Category() {

    }

    public static Category newInstance(JSONObject jsonObject) {
        Category category = new Category();
        category.setId(jsonObject.optInt("id"));
        category.setTitle(jsonObject.optString("title"));
        category.setCount(jsonObject.optString("count"));
        category.setImageURL(jsonObject.optString("image"));
        category.setType(jsonObject.optString("type"));
        return category;
    }

    public static Category newInstance(JSONObject jsonObject, boolean isMovie) {
        Category category = new Category();
        category.setId(jsonObject.optInt("category_id"));
        category.setTitle(jsonObject.optString("title"));
        category.setCount(jsonObject.optString("count"));
        category.setImageURL(jsonObject.optString("image"));
        category.setType(jsonObject.optString("type"));
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}
