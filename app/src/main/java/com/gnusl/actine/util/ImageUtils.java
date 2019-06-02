package com.gnusl.actine.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import com.gnusl.actine.application.Atcine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ImageUtils {


    private static final String PROFILE_PICTURE_FILE_NAME = "profile.jpg";

    public static String ToBase64ImageString(Bitmap bitmap) {

        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] toByteArray = baos.toByteArray();

            return Base64.encodeToString(toByteArray, Base64.DEFAULT);
        }
        return "";
    }

    public static File createCameraTempPhotoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Atcine.getApplicationInstance().getFilesDir();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static Bitmap getUserProfilePicture() throws IOException {

        ContextWrapper cw = new ContextWrapper(Atcine.getApplicationInstance());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File profilePicPath = new File(directory, PROFILE_PICTURE_FILE_NAME);

        return BitmapFactory.decodeFile(profilePicPath.getAbsolutePath());
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
