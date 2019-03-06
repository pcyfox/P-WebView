package com.eims.weblib.cookie;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

public class CookieHelper {
    /**
     * 同步Cookie
     *
     * @param url
     * @param cookie 键值对格式格式存储，不能直接将整个cookie存储：如 String cookie="uid=21233"
     *               如需设置多个，需要多次调用。
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static  void synCookies(String url, String cookie) {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);//cookies是在HttpClient中获得的cookie
            cookieManager.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 当账号切换及WebView所在页面销毁时都应该调用
     * 清除Cookie
     *
     * @param context
     */
    public static void removeCookie(Context context, ValueCallback<Boolean> callback) {
        CookieManager.getInstance().removeAllCookies(callback);
    }

}
