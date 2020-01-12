package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.REQUEST_VIDEO_PERMISSIONS
import com.xiugechen.reading_app.Data.VideoCapture
import com.xiugechen.reading_app.R

abstract class ReadingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Config.isBlackMode) {
            if (Config.isHorizontallySwipe) {
                setTheme(R.style.DarkTheme_SwipeRead)
            }
            else {
                setTheme(R.style.DarkTheme_VerticallyRead)
            }
        }
        else {
            if (Config.isHorizontallySwipe) {
                setTheme(R.style.LightTheme_SwipeRead)
            }
            else {
                setTheme(R.style.LightTheme_VerticallyRead)
            }
        }
    }

    override fun onStop() {
        try { VideoCapture.EndRecordAndSave_FrontCamera() } catch (e: Exception) {}
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