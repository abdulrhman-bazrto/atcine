package com.gnusl.actine.ui.Mobile.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.model.Subtitle;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.custom.GifImageView;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.TimeUtils;
import com.gnusl.actine.util.Utils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class WatchActivity extends AppCompatActivity {


    PlayerView playerView;
    GifImageView loading;
    private ImageView ivSubtitles, ivBack, ivQuality, ivAudio, ivFullScreen, iv_cast_screen;
    private ImageButton ibPlay, ibPause, exo_fast_forward, exo_fast_backward;
    private TextView tvCurProgress, tvTotal;
    private PopupMenu menu;
    private View clForward, clBackward;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    MediaSource mediaSource;

    private Show show;
    private HlsManifest hlsManifest;

    private DefaultTrackSelector defaultTrackSelector;

    private int selectedAudio = 0;
    private int selectedQuality = 0;
    private int selectedSubtitle = 0;
    private boolean isSubtitled = false;
    private boolean isSubtitleAvailable = false;
    private Subtitle selectedSubtitleObject = null;

//    private static final CookieManager DEFAULT_COOKIE_MANAGER;
//    static
//    {
//        DEFAULT_COOKIE_MANAGER = new CookieManager();
//        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER)
//        {
//            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
//        }

//        HlsMediaSource hlsMediaSource =

        setContentView(R.layout.activity_watch);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (getIntent().hasExtra("show")) {
            this.show = (Show) getIntent().getSerializableExtra("show");
            this.show.setDownloadVideoUrl("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");
        }

        init();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hlsManifest != null) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(WatchActivity.this, R.style.CustomPopupTheme);
                    PopupMenu menu = new PopupMenu(ctw, v);

                    try {
                        Field[] fields = menu.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if ("mPopup".equals(field.getName())) {
                                field.setAccessible(true);
                                Object menuPopupHelper = field.get(menu);
                                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                setForceIcons.invoke(menuPopupHelper, true);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < hlsManifest.masterPlaylist.variants.size(); i++) {
                        HlsMasterPlaylist.Variant url = hlsManifest.masterPlaylist.variants.get(i);
                        if (i == selectedQuality) {
                            MenuItem sub = menu.getMenu().add(String.valueOf(url.format.height)).setIcon(R.drawable.icon_check_white);
                        } else {
                            MenuItem sub = menu.getMenu().add(String.valueOf(url.format.height));
                        }
                    }
                    menu.show();
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            for (HlsMasterPlaylist.Variant url : hlsManifest.masterPlaylist.variants) {
                                if (String.valueOf(item.getTitle()).equalsIgnoreCase(String.valueOf(url.format.height))) {

                                    DefaultTrackSelector.Parameters build = defaultTrackSelector.getParameters().buildUpon()
                                            .setMaxVideoBitrate(url.format.bitrate)
                                            .build();
                                    defaultTrackSelector.setParameters(build);
                                }
                            }

                            for (int i = 0; i < hlsManifest.masterPlaylist.variants.size(); i++) {
                                HlsMasterPlaylist.Variant url = hlsManifest.masterPlaylist.variants.get(i);
                                if (String.valueOf(item.getTitle()).equalsIgnoreCase(String.valueOf(url.format.height))) {
                                    selectedQuality = i;
                                }
                            }

                            return true;
                        }
                    });
                }

            }
        });

        ivAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hlsManifest != null) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(WatchActivity.this, R.style.CustomPopupTheme);
                    PopupMenu menu = new PopupMenu(ctw, v);

                    try {
                        Field[] fields = menu.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if ("mPopup".equals(field.getName())) {
                                field.setAccessible(true);
                                Object menuPopupHelper = field.get(menu);
                                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                setForceIcons.invoke(menuPopupHelper, true);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < hlsManifest.masterPlaylist.audios.size(); i++) {
                        HlsMasterPlaylist.Rendition url = hlsManifest.masterPlaylist.audios.get(i);
                        if (i == selectedAudio) {
                            MenuItem sub = menu.getMenu().add(url.format.language).setIcon(R.drawable.icon_check_white);
                        } else {
                            MenuItem sub = menu.getMenu().add(url.format.language);
                        }
                    }
                    menu.show();
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            for (int i = 0; i < hlsManifest.masterPlaylist.audios.size(); i++) {
                                HlsMasterPlaylist.Rendition url = hlsManifest.masterPlaylist.audios.get(i);
                                if (url.format.language.equalsIgnoreCase(String.valueOf(item.getTitle()))) {
                                    selectedAudio = i;
                                }
                            }
                            DefaultTrackSelector.Parameters build = defaultTrackSelector.getParameters().buildUpon()
                                    .setPreferredAudioLanguage(String.valueOf(item.getTitle()))
                                    .build();
                            defaultTrackSelector.setParameters(build);
                            return true;
                        }
                    });
                }
            }
        });

        ivSubtitles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSubtitleAvailable) {
                    Toast.makeText(WatchActivity.this, getString(R.string.no_subtitle), Toast.LENGTH_LONG).show();
                    return;
                }

                ContextThemeWrapper ctw = new ContextThemeWrapper(WatchActivity.this, R.style.CustomPopupTheme);
                PopupMenu menu = new PopupMenu(ctw, v);

                try {
                    Field[] fields = menu.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(menu);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isSubtitled) {
                    menu.getMenu().add(R.string.none).setIcon(R.drawable.icon_check_white);
                } else {
                    menu.getMenu().add(R.string.none);
                }
                for (int i = 0; i < show.getSubtitles().size(); i++) {
                    Subtitle subtitle = show.getSubtitles().get(i);
                    if (selectedSubtitleObject != null &&
                            subtitle.getLabel().equalsIgnoreCase(selectedSubtitleObject.getLabel())) {
                        menu.getMenu().add(subtitle.getLabel()).setIcon(R.drawable.icon_check_white);
                    } else {
                        menu.getMenu().add(subtitle.getLabel());
                    }
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        for (int i = 0; i < show.getSubtitles().size(); i++) {
                            Subtitle subtitle = show.getSubtitles().get(i);
                            selectedSubtitle = 0;
                            if (subtitle.getLabel().equalsIgnoreCase(String.valueOf(item.getTitle()))) {
                                selectedSubtitle = i + 1;
                                selectedSubtitleObject = subtitle;
                                break;
                            }
                        }
                        if (selectedSubtitle == 0) {
                            String hls_url = show.getDownloadVideoUrl();

                            currentWindow = player.getCurrentWindowIndex();
                            playbackPosition = player.getCurrentPosition();

                            Uri uri = Uri.parse(hls_url);
                            DataSource.Factory dataSourceFactory =
                                    new DefaultHttpDataSourceFactory("curl/7.64.1");
                            // Create a HLS media source pointing to a playlist uri.
                            mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                                    .setMinLoadableRetryCount(4)
                                    .createMediaSource(uri);
                            player.setPlayWhenReady(playWhenReady);
                            player.prepare(mediaSource, true, false);
                            player.seekTo(currentWindow, playbackPosition);
                            isSubtitled = false;
                            selectedSubtitleObject = null;
                        } else {
                            if (selectedSubtitleObject != null) {

                                String sub = selectedSubtitleObject.getPath();

                                currentWindow = player.getCurrentWindowIndex();
                                playbackPosition = player.getCurrentPosition();

                                Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                                        null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);
                                MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                                        .createMediaSource(Uri.parse(String.valueOf(sub)), textFormat, C.TIME_UNSET);

                                mediaSource = new MergingMediaSource(mediaSource, textMediaSource);
                                player.setPlayWhenReady(playWhenReady);
                                player.prepare(mediaSource, true, false);
                                player.seekTo(currentWindow, playbackPosition);
                                isSubtitled = true;
                            }
                        }


                        return true;
                    }
                });
            }
        });

        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    ivFullScreen.setImageResource(R.drawable.icon_fullscreen
                    );
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    ivFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
                }

            }
        });

    }


    boolean doubleForward = false;
    boolean doublebackward = false;

    private void init() {
        playerView = findViewById(R.id.video_view);
        loading = findViewById(R.id.loading);
        loading.setGifImageResource(R.drawable.loader);
        ivSubtitles = findViewById(R.id.iv_subtitle);
        ivBack = findViewById(R.id.iv_back);
        ivQuality = findViewById(R.id.iv_quality);
        ivAudio = findViewById(R.id.iv_audio);
        tvCurProgress = findViewById(R.id.tv_cur_progress);
        tvTotal = findViewById(R.id.tv_total);
        ivFullScreen = findViewById(R.id.iv_full_screen);
        clBackward = findViewById(R.id.skip_backward);
        clForward = findViewById(R.id.skip_forward);
        ibPause = findViewById(R.id.exo_pause);
        ibPlay = findViewById(R.id.exo_play);
        exo_fast_forward = findViewById(R.id.exo_fast_forward);
        exo_fast_backward = findViewById(R.id.exo_fast_backward);
        iv_cast_screen = findViewById(R.id.iv_cast_screen);

        Utils.setOnFocusScale(ivSubtitles);
        Utils.setOnFocusScale(ivBack);
        Utils.setOnFocusScale(ivQuality);
        Utils.setOnFocusScale(ivAudio);
        Utils.setOnFocusScale(ivFullScreen);
        Utils.setOnFocusScale(ibPause);
        Utils.setOnFocusScale(ibPlay);
        Utils.setOnFocusScale(iv_cast_screen);
        Utils.setOnFocusScale(exo_fast_forward);
        Utils.setOnFocusScale(exo_fast_backward);
        ibPlay.clearAnimation();
        ibPause.clearAnimation();
        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumePlayer();
                ibPlay.clearAnimation();
                ibPause.clearAnimation();
                ibPause.setVisibility(View.VISIBLE);
                ibPlay.setVisibility(View.INVISIBLE);
            }
        });

        ibPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holdPlayer();
                ibPlay.clearAnimation();
                ibPause.clearAnimation();
                ibPause.setVisibility(View.INVISIBLE);
                ibPlay.setVisibility(View.VISIBLE);
            }
        });

        clBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (doublebackward) {
                    if (player != null) {
                        player.seekTo(player.getCurrentPosition() - 3300);
//                        Toast.makeText(WatchActivity.this, "-10 s", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                doublebackward = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doublebackward = false;
                    }
                }, 500);
                return false;
            }
        });

        clForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (doubleForward) {
                    if (player != null) {
                        player.seekTo(player.getCurrentPosition() + 3300);
                    }
                    return true;
                }
                doubleForward = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleForward = false;
                    }
                }, 500);
                return false;
            }
        });

        exo_fast_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null)
                    player.seekTo(player.getCurrentPosition() - 3300);
            }
        });

        exo_fast_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null)
                    player.seekTo(player.getCurrentPosition() + 3300);
            }
        });

        iv_cast_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
                    return;
                } catch (ActivityNotFoundException activitynotfoundexception) {
                    activitynotfoundexception.printStackTrace();
                }

                try {
                    startActivity(new Intent("android.settings.CAST_SETTINGS"));
                    return;
                } catch (Exception exception1) {
                    Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                }
            }
        });


        playerView.getSubtitleView().setBackgroundResource(R.color.transparent);

        playerView.getSubtitleView().setApplyEmbeddedStyles(false);

        int defaultSubtitleColor = Color.argb(255, 218, 218, 218);
        int outlineColor = Color.argb(255, 43, 43, 43);
        CaptionStyleCompat style =
                new CaptionStyleCompat(defaultSubtitleColor,
                        Color.TRANSPARENT, Color.TRANSPARENT,
                        CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                        outlineColor, null);
        playerView.getSubtitleView().setStyle(style);


        DefaultLoadControl defaultLoadControl = new DefaultLoadControl();


        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        defaultTrackSelector = new DefaultTrackSelector(adaptiveTrackSelection);
        DefaultTrackSelector.Parameters build = new DefaultTrackSelector.ParametersBuilder().build();

        defaultTrackSelector.setParameters(build);
//        player = ExoPlayerFactory.newSimpleInstance(this,
//                new DefaultRenderersFactory(this),
//                defaultTrackSelector,
//                defaultLoadControl);

        player = new SimpleExoPlayer.Builder(this).build();


        //init the player
        playerView.setPlayer(player);

        //-------------------------------------------------
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(this,
                "curl/7.64.1", defaultBandwidthMeter);

        //-----------------------------------------------
        //Create media source

        String hls_url = show.getDownloadVideoUrl();

        Uri uri = Uri.parse(hls_url);

        // Create a HLS media source pointing to a playlist uri.
        mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                .setMinLoadableRetryCount(4)
                .createMediaSource(uri);


        if (show.getSubtitles() != null && show.getSubtitles().size() > 0 && !show.getSubtitles().get(0).getPath().equalsIgnoreCase("")) {
            selectedSubtitleObject = show.getSubtitles().get(0);
            ivSubtitles.setVisibility(View.VISIBLE);
            isSubtitleAvailable = true;
            isSubtitled = true;

            String sub = show.getSubtitles().get(0).getPath();

            Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                    null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);
            MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(sub)), textFormat, C.TIME_UNSET);

            mediaSource = new MergingMediaSource(mediaSource, textMediaSource);
        } else {
            ivSubtitles.setVisibility(View.GONE);
        }

        player.setPlayWhenReady(playWhenReady);
        playerView.setKeepScreenOn(true);
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unAppliedRotationDegrees, float pixelWidthHeightRatio) {

            }

            @Override
            public void onRenderedFirstFrame() {
                hlsManifest = (HlsManifest) player.getCurrentManifest();
                if (hlsManifest != null) {
                    if (hlsManifest.masterPlaylist.audios.size() <= 0) {
                        ivAudio.setVisibility(View.INVISIBLE);
                    } else {
                        ivAudio.setVisibility(View.VISIBLE);
                    }
                    if (hlsManifest.masterPlaylist.variants.size() <= 0) {
                        ivQuality.setVisibility(View.INVISIBLE);
                    } else {
                        ivQuality.setVisibility(View.VISIBLE);
                    }
//                    if (hlsManifest.masterPlaylist.subtitles.size() <= 0) {
//                        ivSubtitles.setVisibility(View.INVISIBLE);
//                    } else {
//                        ivSubtitles.setVisibility(View.VISIBLE);
//                    }
                } else {
                    ivAudio.setVisibility(View.INVISIBLE);
                    ivQuality.setVisibility(View.INVISIBLE);
//                    ivSubtitles.setVisibility(View.INVISIBLE);
                }
                if (player != null) {
                    String time = TimeUtils.formatMillis(player.getDuration());
                    tvTotal.setText(time);
                }
                new Thread(() -> {
                    if (player != null) {
                        while (player != null && player.getCurrentPosition() <= player.getDuration()) {
                            SystemClock.sleep(1000);
                            runOnUiThread(() -> {
                                if (player != null)
                                    tvCurProgress.setText(TimeUtils.formatMillis(player.getCurrentPosition()));
                            });
                        }
                    }
                }).start();

            }
        });


        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {


            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_READY:
                        loading.setVisibility(View.GONE);
                        break;
                    case Player.STATE_BUFFERING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(WatchActivity.this, error.getCause().getMessage(), Toast.LENGTH_LONG).show();
//                Toast.makeText(WatchActivity.this, "error happened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });


//        DataLoader.getRequest(Urls.GetContinue.getLink().replaceAll("%id%", String.valueOf(show.getId())), new ConnectionDelegate() {
//            @Override
//            public void onConnectionError(int code, String message) {
//                Log.d("", "");
//            }
//
//            @Override
//            public void onConnectionError(ANError anError) {
//                Log.d("", "");
//            }
//
//            @Override
//            public void onConnectionSuccess(JSONObject jsonObject) {
//                if (jsonObject.has("duration") && jsonObject.has("window")) {
//
//
//                    final Dialog confirmLoginDialog = new Dialog(WatchActivity.this);
//                    confirmLoginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    if (confirmLoginDialog.getWindow() != null)
//                        confirmLoginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    confirmLoginDialog.setContentView(R.layout.dialog_another_login);
//                    confirmLoginDialog.setCancelable(true);
//
//                    TextView tvMsg = confirmLoginDialog.findViewById(R.id.tv_msg);
//                    tvMsg.setText(getString(R.string.start_from_where_you_left));
//
//                    TextView tvResume = confirmLoginDialog.findViewById(R.id.btn_sign_out);
//                    tvResume.setText(getString(R.string.resume));
//
//                    TextView tvStartOver = confirmLoginDialog.findViewById(R.id.btn_cancel);
//                    tvStartOver.setText(getString(R.string.start_over));
//
//                    Utils.setOnFocusScale(tvResume);
//                    Utils.setOnFocusScale(tvStartOver);
//                    tvResume.requestFocus();
//                    tvResume.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    player.seekTo(jsonObject.optInt("window"), jsonObject.optInt("duration") * 1000);
//                                    player.prepare(mediaSource, false, false);
//                                    confirmLoginDialog.dismiss();
//                                }
//                            });
//
//                        }
//                    });
//
//                    tvStartOver.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    player.seekTo(currentWindow, playbackPosition);
//                                    player.prepare(mediaSource, true, false);
//                                    confirmLoginDialog.dismiss();
//                                }
//                            });
//
//                        }
//                    });
//
//                    confirmLoginDialog.show();
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            player.seekTo(currentWindow, playbackPosition);
//                            player.prepare(mediaSource, true, false);
//                        }
//                    });
//
//                }
//            }
//        });

        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, true, false);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlayer();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onPause() {
        super.onPause();
        holdPlayer();
    }


    @Override
    public void onStop() {
        super.onStop();
//
//        if (Util.SDK_INT > 23) {
//            releasePlayer();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void holdPlayer() {
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    private void resumePlayer() {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
//            SharedPreferencesUtils.saveLatestPlayedPosition(new LatestPlayedPosition(show.getId(), playbackPosition, currentWindow));

            int seconds = (int) (playbackPosition / 1000);

            HashMap<String, String> body = new HashMap<>();
            body.put("movie_id", String.valueOf(show.getId()));
            body.put("duration", String.valueOf(seconds));
            body.put("window", String.valueOf(currentWindow));

            DataLoader.postRequest(Urls.SaveContinue.getLink(), body, null);

            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferencesUtils.saveLatestPlayedShow(show);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
