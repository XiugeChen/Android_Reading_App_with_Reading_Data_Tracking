package com.xiugechen.reading_app.Data

import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.lang.Exception

class DataReader {

    fun readTxt(activity: AppCompatActivity, fileResId: Int): String {
        try {
            val inputStream: InputStream = activity.resources.openRawResource(fileResId)
            return inputStream.bufferedReader().use { it.readText() }

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}