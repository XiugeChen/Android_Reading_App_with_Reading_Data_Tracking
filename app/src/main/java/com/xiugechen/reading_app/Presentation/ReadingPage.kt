package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.REQUEST_VIDEO_PERMISSIONS
import com.xiugechen.reading_app.Data.VideoCapture
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_reading_page.*

class ReadingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("ReadingPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_reading_page)

        addListener()
        addContent()
        startRecording()
    }

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

    private fun addListener() {
        backButton.setOnClickListener {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(this.resources.getInteger(R.integer.vibrate_interval).toLong(),
                VibrationEffect.DEFAULT_AMPLITUDE))

            endRecording()
        }
    }

    private fun addContent() {
        textTitle.text = DataManager.mNextFileDisplay.fileTitle
        textBody.text = DataManager.mNextFileDisplay.fileContent
    }

    private fun startRecording() {
        try {
            VideoCapture.StartRecord_FrontCamera(this)
        }
        catch (e: Exception) {
            popupErrorMsg(e.message)
        }
    }

    private fun endRecording() {
        try {
            VideoCapture.EndRecordAndSave_FrontCamera()
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
        catch (e: Exception) {
            popupErrorMsg(e.message)
        }
    }

    private fun popupErrorMsg(msg: String?) {
        MyPopupWindow.showTextPopup(msg, this, R.id.readingPage) {
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }
}

