package com.gnusl.actine.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gnusl.actine.R;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;


public class VideoActivity extends AppCompatActivity {
    VideoView vvPost;
    ImageView mIvWaiting, mIvMusic;

    String mFileURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent t=getIntent();

        mFileURL =t.getStringExtra("url");

        init();
    }

    private void init(){
        vvPost = (VideoView) findViewById(R.id.vv_post_video);
        try {
            File file = new File(mFileURL);
            // Copy file to temporary file in order to view it.
            File temporaryFile = generateTemporaryFile(file.getName());
            FileUtils.copyFile(file.getAbsolutePath(), temporaryFile.getAbsolutePath());
            previewVideo(temporaryFile, vvPost);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected File generateTemporaryFile(String filename) throws IOException {
        String tempFileName = "20130318_010530_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);

        File tempFile = File.createTempFile(
                tempFileName,       /* prefix     "20130318_010530" */
                filename,           /* filename   "video.3gp" */
                storageDir          /* directory  "/data/sdcard/..." */
        );
        tempFile.setExecutable(true,false);

        return tempFile;
    }

    protected void previewVideo(File file, VideoView videoView) {
        videoView.setVideoPath(file.getAbsolutePath());

        MediaController mediaController = new MediaController(this);

        videoView.setMediaController(mediaController);

        mediaController.setMediaPlayer(videoView);

        videoView.setVisibility(View.VISIBLE);

        videoView.start();
    }


}
