package com.gnusl.actine.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.gnusl.actine.BuildConfig;
import com.gnusl.actine.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Yehia on 3/24/2018.
 */

public class MediaUtils {

    public static final int PICK_IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static Uri photoURI;

    public static void openGallery(Fragment fragment) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        fragment.startActivityForResult(pickIntent, PICK_IMAGE_REQUEST_CODE);
    }

    public static void openGallery(Activity activity) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");

        activity.startActivityForResult(pickIntent, PICK_IMAGE_REQUEST_CODE);
    }

    public static void openCamera(Fragment fragment, Activity activity) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //creating file for camera photo
        File photoFile = null;
        try {
            photoFile = ImageUtils.createCameraTempPhotoFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null &&
                takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            photoURI = FileProvider.getUriForFile(activity,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            fragment.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    public static void openCamera(Activity activity) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //creating file for camera photo
        File photoFile = null;
        try {
            photoFile = ImageUtils.createCameraTempPhotoFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null &&
                takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            photoURI = FileProvider.getUriForFile(activity,
                    //"ai.medicus.android.fileprovider",
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    public static void startCrop(Activity activity, Uri uri) {

        Log.d("profilePicture", "startCrop started..");

        Uri outputUri;
        outputUri = Uri.fromFile(new File(activity.getCacheDir(), "cropped"));

        UCrop uCrop = UCrop.of(uri, outputUri);

        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(activity, R.color.white));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, true);
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR, "Crop image");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(activity, R.color.white));
        }
        uCrop.start(activity);
    }

    public static void startCrop(Uri uri, Activity activity) {

        Log.d("profilePicture", "startCrop started..");

        Uri outputUri;
        outputUri = Uri.fromFile(new File(activity.getCacheDir(), "cropped"));

        UCrop uCrop = UCrop.of(uri, outputUri);

        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(activity, R.color.white));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, true);
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR, "Crop image");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
        uCrop.start(activity);
    }


    private static Uri getLocalBitmapUri(Context context, Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


}
