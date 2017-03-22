package com.commit451.commitspiration.api

import com.commit451.commitspiration.model.WhatTheCommitData
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import java.io.IOException

/**
 * Converts the Html to useful data using Jsoup!
 */
internal class WhatTheCommitBodyConverter : Converter<ResponseBody, WhatTheCommitData> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): WhatTheCommitData {
        //I feel like this is more efficient than loading from String... just a guess though
        val document = Jsoup.parse(value.byteStream(), "utf-8", WhatTheCommitClient.API_URL)
        //TODO do we need to close the response here?
        var element = document.getElementById("content")
        val message = element.child(0).text()
        element = document.getElementsByClass("permalink")[0].child(0)
        return WhatTheCommitData(message, WhatTheCommitClient.API_URL + element.attr("href"))
    }
}
