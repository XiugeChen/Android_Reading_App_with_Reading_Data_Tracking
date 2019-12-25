package com.xiugechen.reading_app.Data

import android.util.Log

object DataManager {
    var participant = Participant("", "")
    var dataReader = DataReader()

    fun printData() {
        Log.d("DataManager", "Data: $participant")
    }
}