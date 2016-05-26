package com.commit451.commitspiration.api

import com.commit451.commitspiration.model.WhatTheCommitData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Gets the commit message
 */
class WhatTheConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        if (type === WhatTheCommitData::class.java) {
            return WhatTheCommitBodyConverter()
        }
        return null
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        //we don't do requests here, sorry
        return null
    }

    //In this way, static factory methods are allowed
    companion object {

        fun create(): WhatTheConverterFactory {
            return WhatTheConverterFactory()
        }
    }
}
