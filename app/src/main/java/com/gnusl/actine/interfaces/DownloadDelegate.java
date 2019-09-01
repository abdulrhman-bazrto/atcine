package com.gnusl.actine.interfaces;

import com.androidnetworking.error.ANError;

public interface DownloadDelegate {

    void onDownloadProgress(String fileDir, String fileName, int progress);

    void onDownloadError(ANError anError);

    void onDownloadSuccess(String fileDir, String fileName);

}
