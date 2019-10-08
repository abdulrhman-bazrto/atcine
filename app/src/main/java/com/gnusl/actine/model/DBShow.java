package com.gnusl.actine.model;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class DBShow implements Serializable {

    @Id(assignable = true)
    private Long id;

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
    private String preWatch;
    private String watchTime;
    private String size;
    private int year;
    private String videoUrl;

    // use in android only
    private boolean isInStorage;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsReminded() {
        return isReminded;
    }

    public void setIsReminded(boolean isReminded) {
        this.isReminded = isReminded;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getPreWatch() {
        return preWatch;
    }

    public void setPreWatch(String preWatch) {
        this.preWatch = preWatch;
    }

    public String getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(String watchTime) {
        this.watchTime = watchTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isInStorage() {
        return isInStorage;
    }

    public void setInStorage(boolean inStorage) {
        isInStorage = inStorage;
    }

    public Show getShowObject() {
        Show show = new Show();
        show.setId(this.getId().intValue());
        show.setTitle(this.getTitle());
        show.setCategory(this.getCategory());
        show.setIsDownloaded(this.getIsDownloaded());
        show.setInStorage(this.isInStorage());
        show.setSize(this.getSize());
        show.setVideoUrl(this.getVideoUrl());
        show.setThumbnailImageUrl(this.getThumbnailImageUrl());

        return show;
    }
}
