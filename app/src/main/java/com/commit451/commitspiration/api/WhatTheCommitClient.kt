package com.commit451.commitspiration.api

import retrofit2.Retrofit


/**
 * Client for http://whatthecommit.com/
 */
object WhatTheCommitClient {

    val API_URL = "http://whatthecommit.com"

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(WhatTheConverterFactory.create())
            .build()

    val service: WhatTheCommit = retrofit.create(WhatTheCommit::class.java);
}
