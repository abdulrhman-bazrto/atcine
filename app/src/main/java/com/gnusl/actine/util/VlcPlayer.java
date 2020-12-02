///*
// * Copyright (c) 2018. Ultratechs.co
// */
//
//package com.gnusl.actine.util;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.res.Configuration;
//import android.net.Uri;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.MediaController;
//import android.widget.VideoView;
//
//import org.videolan.libvlc.IVLCVout;
//import org.videolan.libvlc.LibVLC;
//
//import java.util.ArrayList;
//
//public class VlcPlayer extends Player {
//
//    private static final int SURFACE_BEST_FIT = 0;
//    private static final int SURFACE_FIT_SCREEN = 1;
//    private static final int SURFACE_FILL = 2;
//    private static final int SURFACE_16_9 = 3;
//    private static final int SURFACE_4_3 = 4;
//    private static final int SURFACE_ORIGINAL = 5;
//    private static int CURRENT_SIZE = SURFACE_BEST_FIT;
//    private VideoView vlc_view;
//    private LibVLC vlc_lib;
//    private org.videolan.libvlc.MediaPlayer vlc_media_player;
//    private Activity activity;
//
//    public VlcPlayer(VideoView vlc_view, View loading_layout, Activity activity) {
//        this.vlc_view = vlc_view;
//        this.loading_layout = loading_layout;
//        this.activity = activity;
//    }
//
//    @Override
//    public void start(Uri uri) {
//
//        vlc_view.setVisibility(View.VISIBLE);
//        release();
//
//        try {
//            ArrayList<String> options = new ArrayList<>();
//            options.add("--aout=opensles");
//            options.add("--audio-time-stretch");
//            options.add("-vvv");
//            options.add("--http-reconnect");
//            options.add("--network-caching=" + 6 * 1000);
//            vlc_lib = new LibVLC(vlc_view.getContext(), options);
//
//            vlc_media_player = new org.videolan.libvlc.MediaPlayer(vlc_lib);
//            vlc_media_player.setEventListener(event -> {
//                switch (event.type) {
//                    case org.videolan.libvlc.MediaPlayer.Event.Opening: {
////                        vlc_view.setVisibility(View.VISIBLE);
//                        loading_layout.setVisibility(View.GONE);
//                        if (statusListener != null) {
//                            statusListener.onStart();
//                        }
//                        break;
//                    }
//                    case org.videolan.libvlc.MediaPlayer.Event.EncounteredError:
////                        vlc_view.setVisibility(View.GONE);
//                        loading_layout.setVisibility(View.VISIBLE);
//                        if (statusListener != null) {
//                            statusListener.onError();
//                        }
//                        break;
//                }
//            });
//
//            final IVLCVout vOut = vlc_media_player.getVLCVout();
//            vOut.setVideoView(vlc_view);
//            vOut.attachViews();
//
//            org.videolan.libvlc.Media m = new org.videolan.libvlc.Media(vlc_lib, uri);
//            vlc_media_player.setMedia(m);
//            m.release();
//            vlc_media_player.play();
//
//            updateVideoSurfaces();
//
//            setupControls(vlc_view);
//
//        } catch (Exception e) {
//            vlc_view.setVisibility(View.GONE);
//            loading_layout.setVisibility(View.VISIBLE);
//            if (statusListener != null) {
//                statusListener.onError();
//            }
//        }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    protected void setupControls(View controller) {
//        MediaController mediaController = new MediaController(controller.getContext());
//        mediaController.setAnchorView(controller);
//        ((VideoView) controller).setMediaController(mediaController);
//    }
//
//    @Override
//    public void release() {
//        try {
//
//            if (vlc_media_player != null) {
//                vlc_media_player.stop();
//                vlc_media_player.release();
//                final IVLCVout vOut = vlc_media_player.getVLCVout();
//                vOut.detachViews();
//            }
//
//            if (vlc_lib != null) {
//                vlc_lib.release();
//            }
//
//            vlc_lib = null;
//        } catch (Exception e) {
//            vlc_lib = null;
//        }
//    }
//
//
//    public void updateVideoSurfaces() {
//        int sw = activity.getWindow().getDecorView().getWidth();
//        int sh = activity.getWindow().getDecorView().getHeight();
//
//        if (sw * sh == 0) {
//            return;
//        }
//
//        int sw1 = vlc_view.getWidth();
//        int sh1 = vlc_view.getHeight();
//
//        if (sw1 != sw && sh1 != sh) {
//            sw = sw1;
//            sh = sh1;
//        }
//
//        vlc_media_player.getVLCVout().setWindowSize(sw, sh);
//
//        ViewGroup.LayoutParams lp = vlc_view.getLayoutParams();
//        int mVideoHeight = 0;
//        int mVideoWidth = 0;
//        if (mVideoWidth * mVideoHeight == 0) {
//
//            // Case of OpenGL vouts: handles the placement of the video using MediaPlayer API
//
//            lp.width = sw;
//            lp.height = sh;
//            vlc_view.setLayoutParams(lp);
//            changeMediaPlayerLayout(sw, sh);
//            return;
//        }
//
//        if (lp.width == lp.height && lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
//
//            // We handle the placement of the video using Android View LayoutParams
//
//            vlc_media_player.setAspectRatio(null);
//            vlc_media_player.setScale(0);
//        }
//
//        double dw = sw, dh = sh;
//        final boolean isPortrait = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
//
//        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
//            dw = sh;
//            dh = sw;
//        }
//
//        // compute the aspect ratio
//        double ar, vw;
//        int mVideoVisibleHeight = 0;
//        int mVideoVisibleWidth = 0;
//        int mVideoSarNum = 0;
//        int mVideoSarDen = 0;
//        if (mVideoSarDen == mVideoSarNum) {
//
//            // No indication about the density, assuming 1:1
//
//            vw = mVideoVisibleWidth;
//            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight;
//        } else {
//
//            // Use the specified aspect ratio
//
//            vw = mVideoVisibleWidth * (double) mVideoSarNum / mVideoSarDen;
//            ar = vw / mVideoVisibleHeight;
//        }
//
//        // compute the display aspect ratio
//        double dar = dw / dh;
//
//        switch (CURRENT_SIZE) {
//            case SURFACE_BEST_FIT:
//                if (dar < ar)
//                    dh = dw / ar;
//                else
//                    dw = dh * ar;
//                break;
//            case SURFACE_FIT_SCREEN:
//                if (dar >= ar)
//                    dh = dw / ar;
//                    // horizontal
//                else
//                    dw = dh * ar;
//                // vertical
//                break;
//            case SURFACE_FILL:
//                break;
//            case SURFACE_16_9:
//                ar = 16.0 / 9.0;
//                if (dar < ar)
//                    dh = dw / ar;
//                else
//                    dw = dh * ar;
//                break;
//            case SURFACE_4_3:
//                ar = 4.0 / 3.0;
//                if (dar < ar)
//                    dh = dw / ar;
//                else
//                    dw = dh * ar;
//                break;
//            case SURFACE_ORIGINAL:
//                dh = mVideoVisibleHeight;
//                dw = vw;
//                break;
//        }
//
//        // set display size
//        lp.width = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
//        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
//        vlc_view.setLayoutParams(lp);
//
//        vlc_view.invalidate();
//    }
//
//    private void changeMediaPlayerLayout(int displayW, int displayH) {
//
//        // Change the video placement using the MediaPlayer API
//
//        switch (CURRENT_SIZE) {
//            case SURFACE_BEST_FIT:
//                vlc_media_player.setAspectRatio(null);
//                vlc_media_player.setScale(0);
//                break;
//            case SURFACE_FIT_SCREEN:
//            case SURFACE_FILL: {
//                org.videolan.libvlc.Media.VideoTrack vtrack = vlc_media_player.getCurrentVideoTrack();
//                if (vtrack == null)
//                    return;
//                final boolean videoSwapped = vtrack.orientation == org.videolan.libvlc.Media.VideoTrack.Orientation.LeftBottom
//                        || vtrack.orientation == org.videolan.libvlc.Media.VideoTrack.Orientation.RightTop;
//                if (CURRENT_SIZE == SURFACE_FIT_SCREEN) {
//                    int videoW = vtrack.width;
//                    int videoH = vtrack.height;
//
//                    if (videoSwapped) {
//                        int swap = videoW;
//                        videoW = videoH;
//                        videoH = swap;
//                    }
//                    if (vtrack.sarNum != vtrack.sarDen)
//                        videoW = videoW * vtrack.sarNum / vtrack.sarDen;
//
//                    float ar = videoW / (float) videoH;
//                    float dar = displayW / (float) displayH;
//
//                    float scale;
//                    if (dar >= ar)
//                        scale = displayW / (float) videoW;
//                        // horizontal
//                    else
//                        scale = displayH / (float) videoH;
//                    // vertical
//                    vlc_media_player.setScale(scale);
//                    vlc_media_player.setAspectRatio(null);
//                } else {
//                    vlc_media_player.setScale(0);
//                    vlc_media_player.setAspectRatio(!videoSwapped ? "" + displayW + ":" + displayH
//                            : "" + displayH + ":" + displayW);
//                }
//                break;
//            }
//            case SURFACE_16_9:
//                vlc_media_player.setAspectRatio("16:9");
//                vlc_media_player.setScale(0);
//                break;
//            case SURFACE_4_3:
//                vlc_media_player.setAspectRatio("4:3");
//                vlc_media_player.setScale(0);
//                break;
//            case SURFACE_ORIGINAL:
//                vlc_media_player.setAspectRatio(null);
//                vlc_media_player.setScale(1);
//                break;
//        }
//    }
//}
