package com.eims.weblib.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eims.weblib.interceptor.UrlInterceptor;


public class CustomWebView extends WebView {
    private float startX;
    private float startY;
    private int minHeight = -1;
    private static final String TAG = "MyWebView";
    private OnWebViewActionListener onWebViewActionListener;

    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public CustomWebView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WebSettings settings = this.getSettings(); // 设置WebView属性，能够执行JavaScript脚本
        settings.setJavaScriptEnabled(true);

        setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true); //设定支持缩放
        settings.setLoadsImagesAutomatically(true);//设置自动加载图片
        settings.setDisplayZoomControls(true);  //去掉缩放按钮
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源

        settings.setUseWideViewPort(true);//推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);//超屏自动缩放

        //---------开启存储、缓存-------
        settings.setDomStorageEnabled(true);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);//允许本地缓存

        //-----------关闭滚动条------------
        setHorizontalScrollBarEnabled(false);//水平不显示
        setVerticalScrollBarEnabled(false); //垂直不显示

        //接受第三方cookie
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }


        setWebViewClient(new WebViewClient() {
            /**
             * @param url
             * @return
             */
            private boolean interceptRequest(String url) {
                if (onWebViewActionListener != null) {
                    UrlInterceptor interceptor = onWebViewActionListener.getInterceptor(url);
                    if (interceptor != null) {
                        return interceptor.isIntercept(url);
                    }
                } else {
                    loadUrl(url);
                }

                return false;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onPageStarted(url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onPageFinished(url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return interceptRequest(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

        });


        setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onReceivedTitle(title);
                }
            }


            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                if (onWebViewActionListener != null) {
                    return onWebViewActionListener.onJsAlert(url, message, result);
                }
                return false;
            }


            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                if (onWebViewActionListener != null) {
                    return onWebViewActionListener.onJsPrompt(url, message, defaultValue, result);
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onProgressChanged(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (onWebViewActionListener != null) {
                    return onWebViewActionListener.onShowFileChooser(filePathCallback, fileChooserParams);
                }
                return false;
            }

        });
    }


    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() >= 2) { //多点触控
            getParent().requestDisallowInterceptTouchEvent(true);//屏蔽了父控件
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://webview按下
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE://webview滑动
                float offsetx = Math.abs(event.getX() - startX);
                float offsety = Math.abs(event.getY() - startY);
                if (offsetx > offsety) {
                    getParent().requestDisallowInterceptTouchEvent(true);//屏蔽了父控件
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);//事件传递给父控件
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (minHeight > -1 && getMeasuredHeight() < minHeight) {
            setMeasuredDimension(getMeasuredWidth(), minHeight);
        }
    }

    public void loadDataWithBaseURL(String html) {
        this.loadDataWithBaseURL("about:blank", html, "text/html", "utf-8", null);
    }

    public void removeWebView() {
        setWebViewClient(null);
        setWebChromeClient(null);
        ((ViewGroup) getParent()).removeView(this);
        destroy();
    }


    public void setOnWebViewActionListener(OnWebViewActionListener onWebViewActionListener) {
        this.onWebViewActionListener = onWebViewActionListener;
    }


}

