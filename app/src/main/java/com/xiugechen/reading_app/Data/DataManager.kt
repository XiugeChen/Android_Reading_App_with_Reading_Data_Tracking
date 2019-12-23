package com.xiugechen.reading_app.Data

import android.util.Log

object DataManager {
    var participant: Participant = Participant("", "")

    fun printData() {
        Log.d("DataManager", "Data: " + participant.toString())
    }
}