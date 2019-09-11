package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comment implements Serializable {

    private int id;
    private String profileId;
    private String movieId;
    private String episodeId;
    private String comment;
    private String createdAt;
    private Profile profile;



    public static Comment newInstance(JSONObject jsonObject) {
        Comment comment = new Comment();
        comment.setId(jsonObject.optInt("id"));
        comment.setMovieId(jsonObject.optString("movie_id"));
        comment.setEpisodeId(jsonObject.optString("episode_id"));
        comment.setProfileId(jsonObject.optString("profile_id"));
        comment.setComment(jsonObject.optString("comment"));
        comment.setCreatedAt(jsonObject.optString("created_at"));

        if (jsonObject.has("profile")){
            comment.setProfile(Profile.newInstance(jsonObject.optJSONObject("profile")));
        }
        return comment;
    }

    public static List<Comment> newArray(JSONArray jsonArray) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            comments.add(newInstance(jsonArray.optJSONObject(i)));
        }
        return comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }
}
