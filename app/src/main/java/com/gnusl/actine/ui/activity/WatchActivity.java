package com.gnusl.actine.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.model.Subtitle;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
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

public class WatchActivity extends AppCompatActivity {


    PlayerView playerView;
    ProgressBar loading;
    private ImageView ivSubtitles, ivBack, ivQuality, ivAudio;
    private PopupMenu menu;

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
//                if (hlsManifest != null) {
//                    ContextThemeWrapper ctw = new ContextThemeWrapper(WatchActivity.this, R.style.CustomPopupTheme);
//                    PopupMenu menu = new PopupMenu(ctw, v);
//                    for (HlsMasterPlaylist.HlsUrl url : hlsManifest.masterPlaylist.subtitles) {
//                        MenuItem sub = menu.getMenu().add(url.format.language);
//                    }
//                    menu.show();
//                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            DefaultTrackSelector.Parameters build = defaultTrackSelector.getParameters().buildUpon()
//                                    .setPreferredTextLanguage(String.valueOf(item.getTitle()))
//                                    .build();
//                            defaultTrackSelector.setParameters(build);
//                            return true;
//                        }
//                    });
//                }
                ContextThemeWrapper ctw = new ContextThemeWrapper(WatchActivity.this, R.style.CustomPopupTheme);
                menu = new PopupMenu(ctw, v);
                for (Subtitle subtitle : show.getSubtitles()) {
                    MenuItem sub = menu.getMenu().add(subtitle.getLabel());
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        for (Subtitle subtitle : show.getSubtitles()) {
                            if (subtitle.getLabel().equalsIgnoreCase(item.getTitle().toString())) {

                                releasePlayer();

                                DefaultLoadControl defaultLoadControl = new DefaultLoadControl();

                                TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
                                player = ExoPlayerFactory.newSimpleInstance(
                                        new DefaultRenderersFactory(WatchActivity.this),
                                        new DefaultTrackSelector(adaptiveTrackSelection),
                                        defaultLoadControl);


                                //init the player
                                playerView.setPlayer(player);

                                //-------------------------------------------------
                                DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
                                // Produces DataSource instances through which media data is loaded.
                                dataSourceFactory = new DefaultDataSourceFactory(WatchActivity.this,
                                        Util.getUserAgent(WatchActivity.this, "Exo2"), defaultBandwidthMeter);

                                //-----------------------------------------------
                                //Create media source

                                String hls_url = show.getVideoUrl();
                                Uri uri = Uri.parse(hls_url);

                                DataSource.Factory dataSourceFactory =
                                        new DefaultHttpDataSourceFactory(Util.getUserAgent(WatchActivity.this, "app-name"));
                                // Create a HLS media source pointing to a playlist uri.
                                mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

                                player.setPlayWhenReady(playWhenReady);

                                String sub = subtitle.getPath();
                                Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                                        null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);
                                MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                                        .createMediaSource(Uri.parse(String.valueOf(sub)), textFormat, C.TIME_UNSET);

                                mediaSource = new MergingMediaSource(mediaSource, textMediaSource);
                                player.prepare(mediaSource, true, false);
                                player.seekTo(currentWindow, playbackPosition);

                            }
                        }
                        return true;
                    }
                });
            }
        });

    }

    private void init() {
        playerView = findViewById(R.id.video_view);
        loading = findViewById(R.id.loading);
        ivSubtitles = findViewById(R.id.iv_subtitle);
        ivBack = findViewById(R.id.iv_back);
        ivQuality = findViewById(R.id.iv_quality);
        ivAudio = findViewById(R.id.iv_audio);
    }

    @Override
    public void onStart() {
        super.onStart();
        //--------------------------------------
        //Creating default track selector
        //and init the player

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
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unAppliedRotationDegrees, float pixelWidthHeightRatio) {

            }

            @Override
            public void onRenderedFirstFrame() {
                hlsManifest = (HlsManifest) player.getCurrentManifest();
                if (hlsManifest != null) {
                    if (hlsManifest.masterPlaylist.audios.size() <= 1) {
                        ivAudio.setVisibility(View.INVISIBLE);
                    }
                    if (hlsManifest.masterPlaylist.variants.size() <= 1) {
                        ivQuality.setVisibility(View.INVISIBLE);
                    }
                }
                if (show.getSubtitles().size() <= 1) {
                    ivSubtitles.setVisibility(View.INVISIBLE);
                }
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
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
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
}
