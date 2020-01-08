package com.xiugechen.reading_app.Data

import com.beust.klaxon.Json

data class FileInfo(
    @Json(name = "filename")
    val filename: String,

    @Json(name = "title")
    val fileTitle: String = "No title",

    @Json(name = "description")
    val fileDiscription: String = "No description",

    var readIndicator: ReadIndicator = ReadIndicator.UNREAD
)

enum class ReadIndicator {
    READ,
    UNREAD,
    UNKOWN
}