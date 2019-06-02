package com.gnusl.actine.model;

import org.json.JSONObject;

public class User {
    private int id;
    private String name;
    private String email;
    private String mobile;
    private String language;
    private String birthday;
    private String gender;
    private String social;
    private String socialId;


    public static User parse(JSONObject jsonObject) {
        User user = new User();
        user.setId(jsonObject.optInt("id"));
        user.setName(jsonObject.optString("name"));
        user.setEmail(jsonObject.optString("email"));
        user.setMobile(jsonObject.optString("mobile"));
        user.setLanguage(jsonObject.optString("language"));
        user.setBirthday(jsonObject.optString("birthday"));
        user.setGender(jsonObject.optString("gender"));
        user.setSocial(jsonObject.optString("social"));
        user.setSocialId(jsonObject.optString("socialId"));

        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }
}
