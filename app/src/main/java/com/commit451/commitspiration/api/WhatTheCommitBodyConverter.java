package com.commit451.commitspiration.api;

import com.commit451.commitspiration.model.WhatTheCommitData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Converts the Html to useful data using Jsoup!
 */
class WhatTheCommitBodyConverter implements Converter<ResponseBody, WhatTheCommitData> {

    @Override
    public WhatTheCommitData convert(ResponseBody value) throws IOException {
        //I feel like this is more efficient than loading from String... just a guess though
        Document document = Jsoup.parse(value.byteStream(), "utf-8", WhatTheCommitClient.API_URL);
        Element element = document.getElementById("content");
        String message = element.child(0).text();
        element = document.getElementsByClass("permalink").get(0).child(0);

        return new WhatTheCommitData(message, WhatTheCommitClient.API_URL + element.attr("href"));
    }
}
