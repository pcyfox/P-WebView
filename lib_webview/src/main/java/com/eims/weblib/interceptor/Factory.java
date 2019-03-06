package com.eims.weblib.interceptor;

public interface Factory {
    <T extends UrlInterceptor> T create(Class<T> interceptor);
}
