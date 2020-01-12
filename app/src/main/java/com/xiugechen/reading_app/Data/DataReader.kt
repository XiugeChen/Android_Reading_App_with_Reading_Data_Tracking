package com.xiugechen.reading_app.Data

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.lang.Exception

class DataReader {
    // make sure not add / at the end of each dir
    private val READING_DIR = "reading_files"
    private val EXPRI_DIR = "experiment_files"
    private val INFO_DIR = "file_info"

    private val FILE_INFO = "file_info.json"
    private val AGREEMENT_FILE = "agreement.txt"
    private val INTRO_FILE = "introduction.txt"

    var mFileList = ArrayList<String>()
    var mCachedInfo = HashMap<String, FileInfo>()
    var mCachedBody = HashMap<String, String>()

    var mIntro = Pair("", "")
    var mAgreement = Pair("", "")


    /**
     * Read predefined json file information, introduction and agreement files
     */
    fun init(appActivity: AppCompatActivity) {
        // read all file information
        try {
            val fileInfos = Klaxon().parseArray<FileInfo>( appActivity.assets.open("$INFO_DIR/$FILE_INFO") )

            if (fileInfos != null) {
                for(fileInfo in fileInfos) {
                    mCachedInfo[fileInfo.filename] = fileInfo
                }
            }

        } catch (e: Exception) {
            Log.e("DataReader", "Reading error from file info, error: ${e.message}")
        }

        // read agreement
        val agreement = mCachedInfo[AGREEMENT_FILE]

        mAgreement = if (agreement != null) {
            Pair(agreement.fileTitle, readAssetFile(appActivity, "$EXPRI_DIR/$AGREEMENT_FILE"))
        } else {
            Pair("Agreement", readAssetFile(appActivity, "$EXPRI_DIR/$AGREEMENT_FILE"))
        }

        // read introduction
        val intro = mCachedInfo[INTRO_FILE]

        mIntro = if (intro != null) {
            Pair(intro.fileTitle, readAssetFile(appActivity, "$EXPRI_DIR/$INTRO_FILE"))
        } else {
            Pair("Introduction", readAssetFile(appActivity, "$EXPRI_DIR/$INTRO_FILE"))
        }
    }

    /**
     * Open file resources from assets folder by file path, put read data into cached in DataManager
     */
    fun getReadingFile(appActivity: AppCompatActivity, filename: String) {
        val file = filename.trim()

        if (file.endsWith(".txt")) {
            getTxtReadingFile(appActivity, file)
        }
        else if (file.endsWith(".pdf")) {
            getPdfReadingFile(file)
        }
    }

    /**
     * Open pdf file resources from assets folder by file path, put read data into cached in DataManager
     */
    private fun getPdfReadingFile(filename: String) {
        if (!mCachedInfo.containsKey(filename)) {
            mCachedInfo[filename] = FileInfo(filename)
        }

        if (!mCachedBody.containsKey(filename)) {
            mCachedBody[filename] = "$READING_DIR/$filename"
        }

        mFileList.add(filename)
    }

    /**
     * Open txt file resources from assets folder by file path, put read data into cached in DataManager
     */
    private fun getTxtReadingFile(appActivity: AppCompatActivity, filename: String) {
        if (!mCachedInfo.containsKey(filename)) {
            mCachedInfo[filename] = FileInfo(filename)
        }

        if (!mCachedBody.containsKey(filename)) {
            mCachedBody[filename] = readAssetFile(appActivity, "$READING_DIR/$filename")
        }

        mFileList.add(filename)
    }

    private fun readAssetFile(appActivity: AppCompatActivity, filePath: String): String {
        return try {
            val inputStream: InputStream = appActivity.assets.open(filePath)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e("DataReader", "Reading error from $filePath, error: ${e.message}")
            ""
        }
    }
}