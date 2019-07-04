package com.gnusl.actine.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profile implements Serializable, Parcelable {


    private int id;
    private String name;
    private int userId;
    private String image;
    private String imageUrl;
    private boolean isCurrentProfile = false;

    public static Profile newInstance(JSONObject jsonObject) {
        Profile profile = new Profile();
        profile.setId(jsonObject.optInt("id"));
        profile.setName(jsonObject.optString("name"));
        profile.setUserId(jsonObject.optInt("user_id"));
        profile.setImageUrl(jsonObject.optString("image_url"));

        if (profile.getId() == SharedPreferencesUtils.getCurrentProfile())
            profile.setCurrentProfile(true);
        else
            profile.setCurrentProfile(false);

        return profile;
    }

    public static List<Profile> newList(JSONArray jsonArray) {
        List<Profile> profiles = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            profiles.add(newInstance(jsonArray.optJSONObject(i)));

        return profiles;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.userId);
        dest.writeString(this.image);
        dest.writeString(this.imageUrl);
    }

    public Profile() {
    }

    protected Profile(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.userId = in.readInt();
        this.image = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public void setCurrentProfile(boolean currentProfile) {
        this.isCurrentProfile = currentProfile;
    }

    public boolean isCurrentProfile() {
        return isCurrentProfile;
    }
}
