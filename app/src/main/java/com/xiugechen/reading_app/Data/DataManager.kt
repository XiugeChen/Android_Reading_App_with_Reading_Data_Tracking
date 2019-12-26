package com.xiugechen.reading_app.Data

import android.util.Log

object DataManager {
    var mParticipant = Participant("", "")
    var mDataReader = DataReader()

    fun printData() {
        val currentTime = System.currentTimeMillis()

        Log.d("DataManager", "Data:fullname,gender,timeMs")
        Log.d("DataManager", "Data:$mParticipant,%d".format(currentTime))
    }
}