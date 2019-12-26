package com.xiugechen.reading_app.Data

import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.lang.Exception

class DataReader {
    private var mCachedText = HashMap<Int, String>()

    fun readTxt(activity: AppCompatActivity, fileResId: Int): String {
        if (this.mCachedText.containsKey(fileResId)) {
            val result = this.mCachedText[fileResId]

            if (result != null) return result
        }
        else {
            try {
                val inputStream: InputStream = activity.resources.openRawResource(fileResId)
                val result = inputStream.bufferedReader().use { it.readText() }

                this.mCachedText[fileResId] = result
                return result

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return ""
    }
}