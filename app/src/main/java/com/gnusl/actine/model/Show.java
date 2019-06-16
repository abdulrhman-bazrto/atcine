package com.gnusl.actine.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Show implements Serializable {

    private int id;
    private String title;
    private String description;
    private String section;
    private String category;
    private String showTime;
    private int categoryId;
    private int sectionId;
    private String coverImage;
    private String coverImageUrl;
    private String thumbnailImage;
    private String thumbnailImageUrl;
    private boolean isDownloaded;
    private boolean isLike;
    private boolean isFavourite;
    private boolean isReminded;

    public Show(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.sectionId = jsonObject.optInt("section_id");
        this.categoryId = jsonObject.optInt("category_id");
        this.isLike = jsonObject.optBoolean("is_like");
        this.isFavourite = jsonObject.optBoolean("is_favourite");
        this.isDownloaded = jsonObject.optBoolean("is_downloaded");
        this.isReminded = jsonObject.optBoolean("is_reminded");
        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.showTime = jsonObject.optString("show_time");
        this.thumbnailImage = jsonObject.optString("thumbnail_image");
        this.coverImage = jsonObject.optString("cover_image");
        this.category = jsonObject.optString("category");
        this.section = jsonObject.optString("section");
        this.title = jsonObject.optString("title");
        this.coverImageUrl = jsonObject.optString("cover_image_url");
        this.thumbnailImageUrl = jsonObject.optString("thumbnail_image_url");

    }


    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public boolean getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean getIsReminded() {
        return isReminded;
    }

    public void setIsReminded(boolean isReminded) {
        this.isReminded= isReminded;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
