package com.xiugechen.reading_app.Data

data class FileDisplay(val filename: String, val fileDiscription: String, var readIndicator: ReadIndicator)

enum class ReadIndicator {
    READ,
    UNREAD,
    UNKOWN
}