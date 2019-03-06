package com.eims.weblib.interceptor;


public class InterceptorFactory implements Factory {


    @Override
    public <T extends UrlInterceptor> T create(Class<T> tClass) {
        UrlInterceptor interceptor = null;

        try {
            interceptor = (UrlInterceptor) Class.forName(tClass.getName()).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (interceptor != null) {
            return (T) interceptor;
        }

        return null;
    }
}
