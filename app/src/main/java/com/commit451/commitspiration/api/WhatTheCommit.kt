package com.commit451.commitspiration.api

import com.commit451.commitspiration.model.WhatTheCommitData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * What the commit Retrofit interface
 */
interface WhatTheCommit {

    @GET
    fun getMessage(@Url url: String): Call<WhatTheCommitData>
}
