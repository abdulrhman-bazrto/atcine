package com.gnusl.actine.network;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gnusl.actine.interfaces.WebViewOnFinish;


public class MyWebViewClient extends WebViewClient {

    private WebViewOnFinish webViewOnFinish;


    public MyWebViewClient(WebViewOnFinish webViewOnFinish) {
        super();
        this.webViewOnFinish = webViewOnFinish;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        webViewOnFinish.onFinish(url);
    }
}