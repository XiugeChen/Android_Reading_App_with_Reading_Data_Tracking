package com.xiugechen.reading_app.Data

import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.lang.Exception

class DataReader {
    private var mCachedText = HashMap<Int, Triple<String, String, String>>()

    fun readTxt(activity: AppCompatActivity, fileResId: Int): Triple<String, String, String> {
        if (this.mCachedText.containsKey(fileResId)) {
            val result = this.mCachedText[fileResId]

            if (result != null) return result
        }
        else {
            try {
                val inputStream: InputStream = activity.resources.openRawResource(fileResId)
                val result = readTitleDescripBody(inputStream)

                this.mCachedText[fileResId] = result
                return result

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return Triple("Something wrong with cached file", "", "please try it again")
    }

    private fun readTitleDescripBody(inputStream: InputStream): Triple<String, String, String> {
        var title = ""
        var description = ""
        var body = ""

        var readTitle = false
        var readDescription = false
        var readBody = false


        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }

        for (line in lineList) {
            if (line.startsWith("<title>")) {
                readTitle = true
                readDescription = false
                readBody = false
                continue
            }
            else if (line.startsWith("<description>")) {
                readTitle = false
                readDescription = true
                readBody = false
                continue
            }
            else if (line.startsWith("<body>")) {
                readTitle = false
                readDescription = false
                readBody = true
                continue
            }
            else {
                if (readTitle) {
                    title += line + "\n"
                }

                if (readDescription) {
                    description += line + "\n"
                }

                if (readBody) {
                    body += line + "\n"
                }
            }
        }

        title = title.trim()
        description = description.trim()
        body = body.trim()

        return Triple(title, description, body)
    }
}