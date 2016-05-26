package com.commit451.commitspiration.api;

import com.commit451.commitspiration.model.WhatTheCommitData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Gets the commit message
 */
public class WhatTheConverterFactory extends Converter.Factory {

    public static WhatTheConverterFactory create() {
        return new WhatTheConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == WhatTheCommitData.class) {
            return new WhatTheCommitBodyConverter();
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        //we don't do requests here, sorry
        return null;
    }
}
