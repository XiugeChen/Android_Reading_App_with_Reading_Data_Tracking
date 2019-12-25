package com.xiugechen.reading_app.Data

import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.lang.Exception

class DataReader {
    private var cachedText = HashMap<Int, String>()

    fun readTxt(activity: AppCompatActivity, fileResId: Int): String {
        if (this.cachedText.containsKey(fileResId)) {
            val result = this.cachedText[fileResId]

            if (result != null) return result
        }
        else {
            try {
                val inputStream: InputStream = activity.resources.openRawResource(fileResId)
                val result = inputStream.bufferedReader().use { it.readText() }

                this.cachedText[fileResId] = result
                return result

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return ""
    }
}