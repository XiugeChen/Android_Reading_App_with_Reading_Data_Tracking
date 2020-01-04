package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
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
            MyPopupWindow.showTextPopup(e.message, this, R.id.readingPage) {
                startActivity(Intent(this, FileSelectionPage::class.java))
            }
        }
    }

    private fun endRecording() {
        try {
            VideoCapture.EndRecordAndSave_FrontCamera()
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
        catch (e: Exception) {
            MyPopupWindow.showTextPopup(e.message, this, R.id.readingPage) {
                startActivity(Intent(this, FileSelectionPage::class.java))
            }
        }
    }
}

