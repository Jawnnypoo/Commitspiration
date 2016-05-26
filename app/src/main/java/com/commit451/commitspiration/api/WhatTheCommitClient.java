package com.commit451.commitspiration.api;



import com.commit451.commitspiration.model.WhatTheCommitData;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Client for http://whatthecommit.com/
 */
public class WhatTheCommitClient {

    public static final String API_URL = "http://whatthecommit.com/";

    private static WhatTheCommit sWhatTheCommit;

    public interface WhatTheCommit {
        @GET
        Call<WhatTheCommitData> getMessage(@Url String url);
    }

    public static WhatTheCommit instance() {
        if (sWhatTheCommit == null) {
            OkHttpClient client = new OkHttpClient();
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(WhatTheConverterFactory.create())
                    .client(client)
                    .build();
            sWhatTheCommit = restAdapter.create(WhatTheCommit.class);
        }
        return sWhatTheCommit;
    }
}
