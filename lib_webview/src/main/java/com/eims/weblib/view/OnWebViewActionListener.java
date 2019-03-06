package com.eims.weblib.view;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.eims.weblib.interceptor.UrlInterceptor;

public interface OnWebViewActionListener {
    boolean onShowFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);

    void onPageStarted(String url, Bitmap favicon);

    void onPageFinished(String url);

    UrlInterceptor getInterceptor(String url);

    boolean onJsAlert(String url, String message, JsResult result);

    void onProgressChanged(int newProgress);

    void onReceivedTitle(String title);

    boolean onJsPrompt(String url, String message, String defaultValue, JsPromptResult result);


    class DefaultActionListener implements OnWebViewActionListener {


        @Override
        public boolean onShowFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            return false;
        }

        @Override
        public void onPageStarted(String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(String url) {

        }

        @Override
        public UrlInterceptor getInterceptor(String url) {
            return null;
        }

        @Override
        public boolean onJsAlert(String url, String message, JsResult result) {
            return false;
        }

        @Override
        public void onProgressChanged(int newProgress) {

        }

        @Override
        public void onReceivedTitle(String title) {

        }

        @Override
        public boolean onJsPrompt(String url, String message, String defaultValue, JsPromptResult result) {
            return false;
        }
    }
}
