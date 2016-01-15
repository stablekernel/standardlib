package com.stablekernel.standardlib;

import android.util.Pair;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

import static retrofit.RestAdapter.LogLevel.*;

public class GenericWebServiceProvider {
    public enum InterceptorHeaders {
        ACCEPT("Accept"),
        APPLICATION_FORM("application/x-www-form-urlencoded"),
        APPLICATION_JSON("application/json"),
        AUTHORIZATION("Authorization"),
        CONTENT_TYPE("Content-Type"),
        X_CLIENTID("X-ClientID"),
        X_TIMESTAMP("X-Timestamp");

        private final String header;

        InterceptorHeaders(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }

    public static <T> T getWebService(String endpoint,
                                      Class<T> clazz,
                                      List<Pair<InterceptorHeaders, String>> headers,
                                      Converter converter,
                                      RefreshManager refreshManager) {

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new HeadersInterceptor(headers, refreshManager));
        RestAdapter.Builder builder = new RestAdapter.Builder().setClient(new OkClient(client))
                                                               .setEndpoint(endpoint);

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(FULL);
        }

        if (converter != null) {
            builder.setConverter(converter);
        }

        return builder
                .build()
                .create(clazz);
    }

    public static abstract class RefreshManager {
        public abstract String getAccessToken();
        public abstract void refresh();
        public abstract void logout();
    }

    public static class HeadersInterceptor implements Interceptor {

        private static final String TAG = "HeadersInterceptor";
        private final List<Pair<InterceptorHeaders, String>> headers;
        private final Object lockObject = new Object();
        private RefreshManager refreshManager;

        public HeadersInterceptor(List<Pair<InterceptorHeaders, String>> headers, RefreshManager refreshManager) {
            this.headers = headers;
            this.refreshManager = refreshManager;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = null;
            if (refreshManager != null) {
                token = refreshManager.getAccessToken();
            }

            Request.Builder builder = chain.request().newBuilder();

            for (Pair<InterceptorHeaders, String> header : headers) {
                builder.addHeader(header.first.getHeader(), header.second);
            }
            Response response = chain.proceed(builder.build());

            if (refreshManager != null) {
                if (response.code() == 401) {
                    synchronized (lockObject) {
                        String currentToken = refreshManager.getAccessToken();
                        if (currentToken != null && currentToken.equals(token)) {
                            return performRefresh(chain, builder);
                        }
                    }
                }
            }
            return response;
        }

        private Response performRefresh(Chain chain, Request.Builder builder) throws IOException {
            try {
                refreshManager.refresh();
                return chain.proceed(builder.build());

            } catch (RetrofitError e) {
                android.util.Log.e(TAG, e.getLocalizedMessage(), e);

                retrofit.client.Response refreshResponse = e.getResponse();
                if (refreshResponse != null) {
                    int status = refreshResponse.getStatus();

                    if (status == 401) {
                        refreshManager.logout();
                    }
                }
            }
            return null;
        }
    }
}
