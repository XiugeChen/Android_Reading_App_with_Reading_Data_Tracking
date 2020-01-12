package com.xiugechen.reading_app.Data

import android.app.Activity
import android.util.Log
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataManager {
    private const val TXT_VERTI_HEADER = "TxtVertiData:Fullname,Gender,TimeMs,Filename,NewX,NewY,OldX,OldY\n"
    private const val PDF_VERTI_HEADER = "PdfVertiData:Fullname,Gender,TimeMs,Filename,Page,PositionOffset\n"
    private const val HORI_HEADER = "HoriData:Fullname,Gender,TimeMs,Filename,Page,PageCount\n"

    private var outputFilepath = "Default.txt"

    var mParticipant = Participant("", "")

    var mDataReader = DataReader()

    var mNextFile = ""

    fun printTxtVertiData(
        appActivity: Activity,
        x: Int = 0,
        y: Int = 0,
        oldX: Int = 0,
        oldY: Int = 0
    ) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        val outputMsg = "TxtVertiData:$mParticipant,%d,%s,%d,%d,%d,%d\n"
            .format(System.currentTimeMillis(), cleanedFilename, x, y, oldX, oldY)

        val filename = "%s_%s_VertiTxt_${System.currentTimeMillis()}.txt"
            .format(mParticipant.mFullname, cleanedFilename)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, isTxt = true, isHori = false)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to txt verti output file failed, write to terminal")

            Log.e("printTxtVertiData", outputMsg)
        }
    }

    fun printPdfVertiData(
        appActivity: Activity,
        page: Int,
        positionOffset: Float
    ) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        val outputMsg = "PdfVertiData:$mParticipant,%d,%s,%d,%f\n"
            .format(System.currentTimeMillis(), cleanedFilename, page, positionOffset)

        val filename = "%s_%s_VertiPdf_${System.currentTimeMillis()}.txt"
            .format(mParticipant.mFullname, cleanedFilename)

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, isTxt = false, isHori = false)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to pdf verti output file failed, write to terminal")

            Log.e("printPdfVertiData", outputMsg)
        }
    }

    fun printHoriData(
        appActivity: Activity,
        page: Int,
        pageCount: Int,
        isTxt: Boolean
    ) {
        val cleanedFilename = mNextFile.replace(Regex("\\..*"), "")

        var filename: String
        var outputMsg: String

        if (isTxt) {
            filename = "%s_%s_HoriTxt_${System.currentTimeMillis()}.txt"
                .format(mParticipant.mFullname, cleanedFilename)
            outputMsg = "TxtHoriData:$mParticipant,%d,%s,%d,%d\n"
                .format(System.currentTimeMillis(), cleanedFilename, page, pageCount)
        }
        else {
            filename = "%s_%s_HoriPdf_${System.currentTimeMillis()}.txt"
                .format(mParticipant.mFullname, cleanedFilename)
            outputMsg = "PdfHoriTxtData:$mParticipant,%d,%s,%d,%d\n"
                .format(System.currentTimeMillis(), cleanedFilename, page, pageCount)
        }

        try {
            if (!outputFilepath.contains(filename)) {
                getOutputPath(appActivity, filename, isTxt = isTxt, isHori = true)
            }

            val outputFile = File(outputFilepath)

            outputFile.appendText(outputMsg)
        } catch (e: Exception) {
            Log.e("DataManager", "Write to hori output file failed, write to terminal")

            Log.e("printHoriData", outputMsg)
        }
    }

    /**
     * renew output file with new filename, and init output file with header
     */
    private fun getOutputPath(
        appActivity: Activity,
        filename: String,
        isTxt: Boolean,
        isHori: Boolean
    ) {
        val dir = appActivity.getExternalFilesDir(null)

        if (dir == null) {
            outputFilepath = filename
        } else {
            outputFilepath = "${dir.absolutePath}/$filename"
        }

        val outputFile = File(outputFilepath)

        if (isHori) {
            if (isTxt) {
                outputFile.writeText("Txt$HORI_HEADER")
            }
            else {
                outputFile.writeText("Pdf$HORI_HEADER")
            }
            outputFile.writeText(HORI_HEADER)
        }
        else {
            if (isTxt) {
                outputFile.writeText(TXT_VERTI_HEADER)
            }
            else {
                outputFile.writeText(PDF_VERTI_HEADER)
            }
        }
    }
}