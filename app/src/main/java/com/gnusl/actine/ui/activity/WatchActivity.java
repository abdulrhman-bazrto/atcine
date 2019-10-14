package com.gnusl.actine.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

public class WatchActivity extends AppCompatActivity {


    PlayerView playerView;
    ProgressBar loading;
    private ImageView ivSubtitles;
    private PopupMenu menu;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    MediaSource mediaSource;

    private Show show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        if (getIntent().hasExtra("show")) {
            this.show = (Show) getIntent().getSerializableExtra("show");
        }

        init();

        ivSubtitles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = new PopupMenu(WatchActivity.this, v);
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
                                Handler mainHandler = new Handler();
                                mediaSource = new HlsMediaSource(uri,
                                        dataSourceFactory, mainHandler, null);

//                                player.prepare(mediaSource);

                                player.setPlayWhenReady(playWhenReady);

                                String sub = subtitle.getPath();
                                Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                                        null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);
                                MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                                        .createMediaSource(Uri.parse(String.valueOf(sub)), textFormat, C.TIME_UNSET);

                                mediaSource = new MergingMediaSource(mediaSource, textMediaSource);
//                                player.prepare(mediaSource);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        //--------------------------------------
        //Creating default track selector
        //and init the player

        DefaultLoadControl defaultLoadControl = new DefaultLoadControl();

        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(adaptiveTrackSelection),
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
        Handler mainHandler = new Handler();
        mediaSource = new HlsMediaSource(uri,
                dataSourceFactory, mainHandler, null);

        player.prepare(mediaSource);

        player.setPlayWhenReady(playWhenReady);
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
