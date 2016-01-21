package com.stablekernel.standardlib;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AuthRetryInterceptor implements Interceptor {

    private static final String TAG = "AuthRetryInterceptor";
    private final Object lockObject = new Object();


    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = getAccessToken();
        Request.Builder builder = chain.request().newBuilder();
        Response response = chain.proceed(builder.build());
        if (response.code() == 401) {
            synchronized (lockObject) {
                String currentToken = getAccessToken();
                if (currentToken != null && currentToken.equals(token)) {
                    return performRefresh(chain, builder);
                }
            }
        }
        return response;
    }

    private Response performRefresh(Chain chain, Request.Builder builder) throws IOException {
        try {
            refreshAccessToken();
            return chain.proceed(builder.build());
        } catch (IOException e) {
            performRefreshFailure();
            throw e;
        }
    }

    public String getAccessToken(){ return  ""; }

    public void refreshAccessToken() {}

    public void performRefreshFailure() {}
}