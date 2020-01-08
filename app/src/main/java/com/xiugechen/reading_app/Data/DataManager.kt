package com.xiugechen.reading_app.Data

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataManager {
    private val SCROLL_HEADER = "ScrollData:Fullname,Gender,TimeMs,Filename,NewX,NewY,OldX,OldY\n"
    private val FLIP_HEADER = "FlipData:Fullname,Gender,TimeMs,Filename,FlipAction\n"

    private var outputFilepath = "Default.txt"

    var mParticipant = Participant("", "")

    var mDataReader = DataReader()

    var mNextFile = ""

    fun printScrollData(appActivity: AppCompatActivity, x: Int=0, y: Int=0, oldX: Int=0, oldY: Int=0) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        val outputMsg = "ScrollData:$mParticipant,%d,%s,%d,%d,%d,%d\n"
            .format(System.currentTimeMillis(), cleanedFilename, x, y, oldX, oldY)

        val current = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

        val filename = "%s_%s_%s_scroll.txt"
            .format(mParticipant.mFullname, cleanedFilename, current)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, true)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to scroll output file failed, write to terminal")

            Log.i("printScrollData", outputMsg)
        }
    }

    fun printFlipData(appActivity: AppCompatActivity, flipAction: String) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        val outputMsg = "FlipData:$mParticipant,%d,%s,%s\n"
            .format(System.currentTimeMillis(), cleanedFilename, flipAction)

        val current = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

        val filename = "%s_%s_%s_flip.txt"
            .format(mParticipant.mFullname, cleanedFilename, current)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, false)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to flip output file failed, write to terminal")

            Log.i("printFlipData", outputMsg)
        }
    }

    /**
     * renew output file with new filename, and init output file with header
     */
    private fun getOutputPath(appActivity: AppCompatActivity, filename: String, isScroll: Boolean) {
        val dir = appActivity.getExternalFilesDir(null)

        if (dir == null) {
            outputFilepath = filename
        } else {
            outputFilepath = "${dir.absolutePath}/$filename"
        }

        val outputFile = File(outputFilepath)

        if(isScroll) {
            outputFile.writeText(SCROLL_HEADER)
        }
        else {
            outputFile.writeText(FLIP_HEADER)
        }
    }
}