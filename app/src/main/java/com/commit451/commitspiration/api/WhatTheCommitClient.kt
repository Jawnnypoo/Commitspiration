package com.commit451.commitspiration.api

import retrofit2.Retrofit


/**
 * Client for http://whatthecommit.com/
 */
object WhatTheCommitClient {

    val API_URL = "http://whatthecommit.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(WhatTheConverterFactory.create())
        .baseUrl(API_URL)
        .build()

    val service : WhatTheCommit = retrofit.create(WhatTheCommit::class.java);
}
