package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabObject implements Serializable {

    private String text;
    private int iconRes;

    private boolean isSelected = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public TabObject(String text, int iconRes) {
        this.text = text;
        this.iconRes = iconRes;
    }

    public TabObject(String text, int iconRes, boolean isSelected) {
        this.text = text;
        this.iconRes = iconRes;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
