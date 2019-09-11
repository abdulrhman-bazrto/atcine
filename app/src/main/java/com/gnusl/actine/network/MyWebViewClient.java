package com.gnusl.actine.network;

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
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;


    }

    @Override
    public void onPageFinished(WebView view, String url) {
        webViewOnFinish.onFinish(url);
    }
}