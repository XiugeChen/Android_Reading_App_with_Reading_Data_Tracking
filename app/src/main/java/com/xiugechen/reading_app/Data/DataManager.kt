package com.xiugechen.reading_app.Data

import android.util.Log

object DataManager {
    var participant: Participant = Participant("", "")
    var dataReader: DataReader = DataReader()

    fun printData() {
        Log.d("DataManager", "Data: " + participant.toString())
    }
}