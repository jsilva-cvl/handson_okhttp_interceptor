package com.jsilva.okhttp_interceptor;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class InterceptRequest implements Interceptor {

    /**
     * URL for the new resource
     */
    private static final String NEW_URL = "https://m.media-amazon.com/images/M/MV5BOTg4ZTNkZmUtMzNlZi00YmFjLTk1MmUtNWQwNTM0YjcyNTNkXkEyXkFqcGdeQXVyNjg2NjQwMDQ@._V1_UX182_CR0,0,182,268_AL_.jpg";

    @Override
    public Response intercept(Chain chain) throws IOException {

        // creates a client and a request to perform the Http call
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request.Builder requestBuilder = new Request.Builder().url(NEW_URL).cacheControl(CacheControl.FORCE_NETWORK);

        // copies all the original headers into the new request
        Headers headers = chain.request().headers();
        for (String name : headers.names()) {
            requestBuilder.addHeader(name, headers.get(name));
        }

        //returns a response
        return okHttpClient.newCall(requestBuilder.build()).execute();
    }
}