package com.stablekernel.standardlib;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public abstract class AuthRetryInterceptor implements Interceptor {

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
            IOException exception = refreshAccessToken();
            Response response = chain.proceed(builder.build());
            if (response.code() < 200 || response.code() >= 300) {
                throw exception;
            }
            return response;
        } catch (IOException e) {
            performRefreshFailure(e);
            throw e;
        }
    }

    public abstract String getAccessToken();

    public abstract IOException refreshAccessToken();

    public abstract void performRefreshFailure(IOException e);
}
