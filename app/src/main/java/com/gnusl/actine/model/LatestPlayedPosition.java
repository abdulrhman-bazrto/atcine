package com.gnusl.actine.model;

import org.json.JSONObject;

public class LatestPlayedPosition {
    private int showId;
    private int windowIndex;
    private long position;

    public LatestPlayedPosition(int showId, long position,int windowIndex) {
        this.showId = showId;
        this.position = position;
        this.windowIndex = windowIndex;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getWindowIndex() {
        return windowIndex;
    }

    public void setWindowIndex(int windowIndex) {
        this.windowIndex = windowIndex;
    }
}
