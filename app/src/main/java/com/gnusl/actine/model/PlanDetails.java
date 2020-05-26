package com.gnusl.actine.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlanDetails implements Serializable {


    private int id;
    private String title;
    private String basic;
    private String standard;
    private String premium;
    private String type;
    private boolean isBasicSelected;
    private boolean isPremiumSelected;
    private boolean isStandardSelected;

    public static PlanDetails newInstance(JSONObject jsonObject) {
        PlanDetails planDetails = new PlanDetails();
        planDetails.setId(jsonObject.optInt("id"));
        planDetails.setTitle(jsonObject.optString("title"));
        planDetails.setStandard(jsonObject.optString("Standard"));
        planDetails.setBasic(jsonObject.optString("Basic"));
        planDetails.setPremium(jsonObject.optString("Premium"));
        planDetails.setType(jsonObject.optString("type"));

        return planDetails;
    }

    public static List<PlanDetails> newList(JSONArray jsonArray) {
        List<PlanDetails> profiles = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            profiles.add(newInstance(jsonArray.optJSONObject(i)));

        return profiles;
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

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBasicSelected() {
        return isBasicSelected;
    }

    public void setBasicSelected(boolean basicSelected) {
        isBasicSelected = basicSelected;
    }

    public boolean isPremiumSelected() {
        return isPremiumSelected;
    }

    public void setPremiumSelected(boolean premiumSelected) {
        isPremiumSelected = premiumSelected;
    }

    public boolean isStandardSelected() {
        return isStandardSelected;
    }

    public void setStandardSelected(boolean standardSelected) {
        isStandardSelected = standardSelected;
    }
}
