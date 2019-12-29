package com.xiugechen.reading_app.Data

data class FileDisplay(val filename: String, val fileTitle: String, val fileDiscription: String, val fileContent: String, var readIndicator: ReadIndicator)

enum class ReadIndicator {
    READ,
    UNREAD,
    UNKOWN
}