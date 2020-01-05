package com.xiugechen.reading_app.Data

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataManager {
    private val HEADER = "Fullname,Gender,TimeMs,Filename,NewX,NewY,OldX,OldY\n"

    var mParticipant = Participant("", "")
    var mDataReader = DataReader()
    var mCachedFileDisplays = ArrayList<FileDisplay>()
    var mNextFileDisplay = FileDisplay("", "", "", "", ReadIndicator.UNKOWN)
    private var outputFilepath = "Default.txt"

    fun printScrollData(appActivity: AppCompatActivity, x: Int=0, y: Int=0, oldX: Int=0, oldY: Int=0) {
        val cleanedFilename = mNextFileDisplay.filename.replace(Regex("\\..*"), "")

        val outputMsg = "Data:$mParticipant,%d,%s,%d,%d,%d,%d\n"
            .format(System.currentTimeMillis(), cleanedFilename, x, y, oldX, oldY)

        val current = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

        val filename = "%s_%s_%s.txt"
            .format(mParticipant.mFullname, cleanedFilename, current)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to output file failed, write to terminal")

            Log.i("printScrollData", outputMsg)
        }
    }

    /**
     * renew output file with new filename, and init output file with header
     */
    private fun getOutputPath(appActivity: AppCompatActivity, filename: String) {
        val dir = appActivity.getExternalFilesDir(null)

        if (dir == null) {
            outputFilepath = filename
        } else {
            outputFilepath = "${dir.absolutePath}/$filename"
        }

        val outputFile = File(outputFilepath)
        outputFile.writeText(HEADER)
    }
}