package com.xiugechen.reading_app.Data

import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.lang.Exception

class DataReader {
    private var mCachedTxtId = HashMap<Int, Triple<String, String, String>>()

    /**
     * Open resources from assets folder by file path, put read data into cached in DataManager
     */
    fun readTxtByName(activity: AppCompatActivity, filename: String) {
        val filepath = "reading_files/$filename"

        try {
            val inputStream: InputStream = activity.resources.assets.open(filepath)
            val readResult = readTitleDescripBody(inputStream)
            val file = FileDisplay(filename, readResult.first, readResult.second,
                readResult.third, ReadIndicator.UNREAD)

            DataManager.mCachedFileDisplays.add(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Open resources from res folder by res id, return data
     */
    fun readTxtById(activity: AppCompatActivity, fileResId: Int): Triple<String, String, String> {
        if (this.mCachedTxtId.containsKey(fileResId)) {
            val result = this.mCachedTxtId[fileResId]

            if (result != null) return result
        }
        else {
            try {
                val inputStream: InputStream = activity.resources.openRawResource(fileResId)
                val result = readTitleDescripBody(inputStream)

                this.mCachedTxtId[fileResId] = result
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