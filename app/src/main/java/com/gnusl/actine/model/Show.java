package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Show implements Serializable {

    private int id;
    private int seriesId;
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
    private String rottenTomatoes;
    private String imdbRate;
    private int year;
    private boolean isMovie;
    private boolean isSeason;
    private boolean isEpisode;
    private List<Show> seasons;
    private List<Show> episodes;
    private List<Subtitle> subtitles;
    private String videoUrl;
    private String episodeUrl;
    private String downloadVideoUrl;
    private String trailerId;

    // use in android only
    private boolean isInStorage;

    public Show(JSONObject jsonObject, boolean isMovie, boolean isSeason, boolean isEpisode) {
        this.id = jsonObject.optInt("id");
        this.sectionId = jsonObject.optInt("section_id");
        this.categoryId = jsonObject.optInt("category_id");
        if (jsonObject.has("is_like"))
            this.isLike = jsonObject.optBoolean("is_like");
        if (jsonObject.has("is_favourite"))
            this.isFavourite = jsonObject.optBoolean("is_favourite");
        if (jsonObject.has("is_downloaded"))
            this.isDownloaded = jsonObject.optBoolean("is_downloaded");
        if (jsonObject.has("is_reminded"))
            this.isReminded = jsonObject.optBoolean("is_reminded");

        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.showTime = jsonObject.optString("show_time");
        this.thumbnailImage = jsonObject.optString("thumbnail_image");
        this.coverImage = jsonObject.optString("cover_image");
        this.category = jsonObject.optString("category");
        this.section = jsonObject.optString("section");
        this.title = jsonObject.optString("title");
        this.size = jsonObject.optString("size");
        this.coverImageUrl = jsonObject.optString("cover_image_url");
        this.thumbnailImageUrl = jsonObject.optString("thumbnail_image_url").replaceFirst("/image/upload/","/image/upload/w_500,h_700,c_scale/");
        this.preWatch = jsonObject.optString("pre_watch");
        this.watchTime = jsonObject.optString("watch_time");
        this.year = jsonObject.optInt("year");
        this.videoUrl = jsonObject.optString("video_url");
        this.episodeUrl = jsonObject.optString("episode_url");
        this.rottenTomatoes = jsonObject.optString("rotten_tomatoes");
        this.imdbRate = jsonObject.optString("imdb_rate");
        this.downloadVideoUrl = jsonObject.optString("download_video_url");
        this.downloadVideoUrl = jsonObject.optString("download_video_url");
        this.trailerId = jsonObject.optString("trailer_video");
        this.isMovie = isMovie;
        this.isEpisode = isEpisode;
        this.isSeason = isSeason;

        if (jsonObject.has("series_id")) {
            this.seriesId = jsonObject.optInt("series_id");
        }

        if (jsonObject.has("seasons")) {
            this.seasons = Show.newList(jsonObject.optJSONArray("seasons"), false, true, false, isFavourite, isLike, isDownloaded);
        }

        if (jsonObject.has("episodes")) {
            this.episodes = Show.newList(jsonObject.optJSONArray("episodes"), false, false, true, isFavourite, isLike, isDownloaded);
            if (this.isFavourite) {
                for (int i = 0; i < this.episodes.size(); i++)
                    this.episodes.get(i).setIsFavourite(true);
            }
        }

        if (jsonObject.has("subtitles")) {
            this.subtitles = Subtitle.newList(jsonObject.optJSONArray("subtitles"));
        }

    }

    public Show(JSONObject jsonObject, boolean isMovie, boolean isSeason, boolean isEpisode, boolean isFavourite, boolean isLike, boolean isDownloaded) {
        this.id = jsonObject.optInt("id");
        this.sectionId = jsonObject.optInt("section_id");
        this.categoryId = jsonObject.optInt("category_id");
        if (jsonObject.has("is_like"))
            this.isLike = jsonObject.optBoolean("is_like");
        else
            this.isLike = isLike;
        if (jsonObject.has("is_favourite"))
            this.isFavourite = jsonObject.optBoolean("is_favourite");
        else
            this.isFavourite = isFavourite;
        if (jsonObject.has("is_downloaded"))
            this.isDownloaded = jsonObject.optBoolean("is_downloaded");
        else
            this.isDownloaded = isDownloaded;
        if (jsonObject.has("is_reminded"))
            this.isReminded = jsonObject.optBoolean("is_reminded");


        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.showTime = jsonObject.optString("show_time");
        this.thumbnailImage = jsonObject.optString("thumbnail_image");
        this.coverImage = jsonObject.optString("cover_image");
        this.category = jsonObject.optString("category");
        this.size = jsonObject.optString("size");
        this.section = jsonObject.optString("section");
        this.title = jsonObject.optString("title");
        this.coverImageUrl = jsonObject.optString("cover_image_url");
        this.thumbnailImageUrl = jsonObject.optString("thumbnail_image_url").replaceFirst("/image/upload/","/image/upload/w_500,h_700,c_scale/");
        this.preWatch = jsonObject.optString("pre_watch");
        this.watchTime = jsonObject.optString("watch_time");
        this.year = jsonObject.optInt("year");
        this.videoUrl = jsonObject.optString("video_url");
        this.episodeUrl = jsonObject.optString("episode_url");
        this.rottenTomatoes = jsonObject.optString("rotten_tomatoes");
        this.imdbRate = jsonObject.optString("imdb_rate");
        this.downloadVideoUrl = jsonObject.optString("download_video_url");
        this.trailerId = jsonObject.optString("trailer_video");
        this.isMovie = isMovie;
        this.isEpisode = isEpisode;
        this.isSeason = isSeason;

        if (jsonObject.has("series_id")) {
            this.seriesId = jsonObject.optInt("series_id");
        }

        if (jsonObject.has("seasons")) {
            this.seasons = Show.newList(jsonObject.optJSONArray("seasons"), false, true, false, isFavourite, isLike, isDownloaded);
        }

        if (jsonObject.has("episodes")) {
            this.episodes = Show.newList(jsonObject.optJSONArray("episodes"), false, false, true, isFavourite, isLike, isDownloaded);
        }

        if (jsonObject.has("subtitles")) {
            this.subtitles = Subtitle.newList(jsonObject.optJSONArray("subtitles"));
        }

    }

    public Show() {

    }

    public static Show newInstance(JSONObject jsonObject, boolean isMovie, boolean isSeason, boolean isEpisode) {
        return new Show(jsonObject, isMovie, isSeason, isEpisode);
    }

    public static Show newInstance(JSONObject jsonObject, boolean isMovie, boolean isSeason, boolean isEpisode, boolean isFavourite, boolean isLike, boolean isDownloaded) {
        return new Show(jsonObject, isMovie, isSeason, isEpisode, isFavourite, isLike, isDownloaded);
    }

    public static List<Show> newList(JSONArray jsonArray, boolean isMovie, boolean isSeason, boolean isEpisode) {
        List<Show> shows = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            shows.add(newInstance(jsonArray.optJSONObject(i), isMovie, isSeason, isEpisode));
        }
        return shows;
    }

    public static List<Show> newList(JSONArray jsonArray, boolean isMovie, boolean isSeason, boolean isEpisode, boolean isFavourite, boolean isLike, boolean isDownloaded) {
        List<Show> shows = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            shows.add(newInstance(jsonArray.optJSONObject(i), isMovie, isSeason, isEpisode, isFavourite, isLike, isDownloaded));
        }
        return shows;
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

    public boolean getIsMovie() {
        return isMovie;
    }


    public boolean getIsSeason() {
        return isSeason;
    }

    public boolean getIsEpisode() {
        return isEpisode;
    }

    public List<Show> getSeasons() {
        return seasons;
    }

    public void setSeasons(JSONArray jsonArray) {
        this.seasons = newList(jsonArray, false, true, false);
    }

    public List<Show> getEpisodes() {
        return episodes;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public String getVideoUrl() {
        if (this.getIsMovie()) {
            return videoUrl;
        } else if (this.getIsEpisode()) {
            return episodeUrl;
        }
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setEpisodeUrl(String episodeUrl) {
        this.episodeUrl = episodeUrl;
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

    public String getRottenTomatoes() {
        return rottenTomatoes;
    }

    public void setRottenTomatoes(String rottenTomatoes) {
        this.rottenTomatoes = rottenTomatoes;
    }

    public String getImdbRate() {
        return imdbRate;
    }

    public void setImdbRate(String imdbRate) {
        this.imdbRate = imdbRate;
    }

    public String getDownloadVideoUrl() {
        return downloadVideoUrl;
    }

    public void setDownloadVideoUrl(String downloadVideoUrl) {
        this.downloadVideoUrl = downloadVideoUrl;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public DBShow getDBShowObject() {
        DBShow dbShow = new DBShow();
        dbShow.setId((long) this.getId());
        dbShow.setTitle(this.getTitle());
        dbShow.setCategory(this.getCategory());
        dbShow.setIsDownloaded(this.getIsDownloaded());
        dbShow.setInStorage(this.isInStorage());
        dbShow.setSize(this.getSize());
        dbShow.setVideoUrl(this.getVideoUrl());
        dbShow.setThumbnailImageUrl(this.getThumbnailImageUrl());
        dbShow.setVideoUrl(this.getVideoUrl());
        dbShow.setDownloadVideoUrl(this.getDownloadVideoUrl());
        dbShow.setMovie(this.getIsMovie());

        return dbShow;
    }

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }
}
