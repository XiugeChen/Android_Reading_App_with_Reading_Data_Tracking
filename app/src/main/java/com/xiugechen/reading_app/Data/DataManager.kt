package com.xiugechen.reading_app.Data

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataManager {
    private const val TXT_HEADER = "TxtData:Fullname,Gender,TimeMs,Filename,NewX,NewY,OldX,OldY\n"
    private const val PDF_HEADER = "PdfData:Fullname,Gender,TimeMs,Filename,Page,PositionOffset\n"

    private var outputFilepath = "Default.txt"

    var mParticipant = Participant("", "")

    var mDataReader = DataReader()

    var mNextFile = ""

    fun printTxtMoveData(
        appActivity: AppCompatActivity,
        x: Int = 0,
        y: Int = 0,
        oldX: Int = 0,
        oldY: Int = 0
    ) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        val outputMsg = "TxtData:$mParticipant,%d,%s,%d,%d,%d,%d\n"
            .format(System.currentTimeMillis(), cleanedFilename, x, y, oldX, oldY)

        val current = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

        val filename = "%s_%s_%s_Txt.txt"
            .format(mParticipant.mFullname, cleanedFilename, current)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, true)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to txt output file failed, write to terminal")

            Log.e("printTxtMoveData", outputMsg)
        }
    }

    fun printPdfMoveData(
        appActivity: AppCompatActivity,
        page: Int,
        positionOffset: Float
    ) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        val outputMsg = "PdfData:$mParticipant,%d,%s,%d,%f\n"
            .format(System.currentTimeMillis(), cleanedFilename, page, positionOffset)

        val current = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

        val filename = "%s_%s_%s_Pdf.txt"
            .format(mParticipant.mFullname, cleanedFilename, current)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, false)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to pdf output file failed, write to terminal")

            Log.e("printPdfMoveData", outputMsg)
        }
    }

    /**
     * renew output file with new filename, and init output file with header
     */
    private fun getOutputPath(
        appActivity: AppCompatActivity,
        filename: String,
        isTxt: Boolean
    ) {
        val dir = appActivity.getExternalFilesDir(null)

        if (dir == null) {
            outputFilepath = filename
        } else {
            outputFilepath = "${dir.absolutePath}/$filename"
        }

        val outputFile = File(outputFilepath)

        if (isTxt) {
            outputFile.writeText(TXT_HEADER)
        }
        else {
            outputFile.writeText(PDF_HEADER)
        }
    }
}