package com.gnusl.actine.ui.Mobile.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.util.Player;
import com.gnusl.actine.util.VlcPlayer;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class WatchActivity2 extends AppCompatActivity {
    VideoView vlc_view;
    private Show show;
    private VlcPlayer vlc_player;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_watch2);

        if (getIntent().hasExtra("show")) {
            this.show = (Show) getIntent().getSerializableExtra("show");
        }
        init();
    }

    private void init(){
        vlc_view = (VideoView) findViewById(R.id.vlc_view);

        vlc_player = new VlcPlayer(vlc_view, vlc_view, this);
        Uri uri = Uri.parse(show.getDownloadVideoUrl());
        startVlcPlayer(uri);

    }

    private void startVlcPlayer(Uri uri) {
        vlc_player.setStatusListener(new Player.StatusListener() {
            @Override
            public void onStart() {
//                Toast.makeText(getActivity(), "vlc ok", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
            }
        });
        vlc_player.start(uri);
    }

}
