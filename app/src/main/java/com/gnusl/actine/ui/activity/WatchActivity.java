package com.gnusl.actine.ui.activity;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.util.TimeUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
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
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WatchActivity extends AppCompatActivity {


    PlayerView playerView;
    ProgressBar loading;
    private ImageView ivSubtitles, ivBack, ivQuality, ivAudio, ivFullScreen;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        HlsMasterPlaylist.HlsUrl url = hlsManifest.masterPlaylist.variants.get(i);
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
                            for (HlsMasterPlaylist.HlsUrl url : hlsManifest.masterPlaylist.variants) {
                                if (String.valueOf(item.getTitle()).equalsIgnoreCase(String.valueOf(url.format.height))) {

                                    DefaultTrackSelector.Parameters build = defaultTrackSelector.getParameters().buildUpon()
                                            .setMaxVideoBitrate(url.format.bitrate)
                                            .build();
                                    defaultTrackSelector.setParameters(build);
                                }
                            }

                            for (int i = 0; i < hlsManifest.masterPlaylist.variants.size(); i++) {
                                HlsMasterPlaylist.HlsUrl url = hlsManifest.masterPlaylist.variants.get(i);
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
                        HlsMasterPlaylist.HlsUrl url = hlsManifest.masterPlaylist.audios.get(i);
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
                                HlsMasterPlaylist.HlsUrl url = hlsManifest.masterPlaylist.audios.get(i);
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

                    if (selectedSubtitle == -1) {
                        menu.getMenu().add("None").setIcon(R.drawable.icon_check_white);
                    } else {
                        menu.getMenu().add("None");
                    }
                    for (int i = 0; i < hlsManifest.masterPlaylist.subtitles.size(); i++) {
                        HlsMasterPlaylist.HlsUrl url = hlsManifest.masterPlaylist.subtitles.get(i);
                        if (i == selectedSubtitle) {
                            MenuItem sub = menu.getMenu().add(url.format.language).setIcon(R.drawable.icon_check_white);
                        } else {
                            MenuItem sub = menu.getMenu().add(url.format.language);
                        }
                    }
                    menu.show();
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (String.valueOf(item.getTitle()).equalsIgnoreCase("None")) {
                                DefaultTrackSelector.Parameters build = defaultTrackSelector.getParameters().buildUpon()
                                        .setRendererDisabled(C.TRACK_TYPE_VIDEO, true)
                                        .build();
                                defaultTrackSelector.setParameters(build);
                                selectedSubtitle = -1;
                                return true;
                            }
                            for (int i = 0; i < hlsManifest.masterPlaylist.subtitles.size(); i++) {
                                HlsMasterPlaylist.HlsUrl url = hlsManifest.masterPlaylist.subtitles.get(i);
                                if (url.format.language.equalsIgnoreCase(String.valueOf(item.getTitle()))) {
                                    selectedSubtitle = i;
                                }
                            }

                            DefaultTrackSelector.Parameters build = defaultTrackSelector.getParameters().buildUpon()
                                    .setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
                                    .setPreferredTextLanguage(String.valueOf(item.getTitle()))
                                    .build();
                            defaultTrackSelector.setParameters(build);
                            return true;
                        }
                    });
                }
            }
        });

        ivFullScreen.setOnClickListener(new View.OnClickListener() {
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
        ivSubtitles = findViewById(R.id.iv_subtitle);
        ivBack = findViewById(R.id.iv_back);
        ivQuality = findViewById(R.id.iv_quality);
        ivAudio = findViewById(R.id.iv_audio);
        tvCurProgress = findViewById(R.id.tv_cur_progress);
        tvTotal = findViewById(R.id.tv_total);
        ivFullScreen = findViewById(R.id.iv_full_screen);
        clBackward = findViewById(R.id.skip_backward);
        clForward = findViewById(R.id.skip_forward);


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
//                        Toast.makeText(WatchActivity.this, "+10 s", Toast.LENGTH_SHORT).show();
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


        playerView.getSubtitleView().setBackgroundResource(R.color.transparent);

        playerView.getSubtitleView().setApplyEmbeddedStyles(false);

        int defaultSubtitleColor = Color.argb(255, 218, 218, 218);
        int outlineColor = Color.argb(255, 43, 43, 43);
//        Typeface subtitleTypeface = Typeface.createFromAsset(getAssets(), "fonts/droid.ttf");
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
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                defaultTrackSelector,
                defaultLoadControl);


        //init the player
        playerView.setPlayer(player);

        //-------------------------------------------------
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter);

        //-----------------------------------------------
        //Create media source

        String hls_url = show.getVideoUrl();

        Uri uri = Uri.parse(hls_url);
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "app-name"));
        // Create a HLS media source pointing to a playlist uri.
        mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);


        player.prepare(mediaSource);

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
                    }
                    if (hlsManifest.masterPlaylist.variants.size() <= 0) {
                        ivQuality.setVisibility(View.INVISIBLE);
                    }
                    if (hlsManifest.masterPlaylist.subtitles.size() <= 0) {
                        ivSubtitles.setVisibility(View.INVISIBLE);
                    }
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
//                Toast.makeText(WatchActivity.this, error.getCause().getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(WatchActivity.this, "error happened", Toast.LENGTH_SHORT).show();
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
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, true, false);

        player.getAudioAttributes();

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
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
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
