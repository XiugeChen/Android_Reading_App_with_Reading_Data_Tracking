package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.REQUEST_VIDEO_PERMISSIONS
import com.xiugechen.reading_app.Data.VideoCapture
import com.xiugechen.reading_app.R

abstract class ReadingPage : AppCompatActivity() {

    override fun onStop() {
        endRecording()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_VIDEO_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission granted
                    try {
                        VideoCapture.init(this)
                    }
                    catch (e: Exception) {
                        popupErrorMsg(e.message)
                    }
                } else {
                    // permission denied
                    popupErrorMsg("This app need video and audio permission to run, please open them")
                }
            }
        }
    }

    protected fun startRecording() {
        try {
            VideoCapture.StartRecord_FrontCamera(this, DataManager.mNextFile)
        }
        catch (e: Exception) {
            popupErrorMsg(e.message)
        }
    }

    protected fun endRecording() {
        try {
            VideoCapture.EndRecordAndSave_FrontCamera()
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
        catch (e: Exception) {
            popupErrorMsg(e.message)
        }
    }

    abstract fun popupErrorMsg(msg: String?)
}