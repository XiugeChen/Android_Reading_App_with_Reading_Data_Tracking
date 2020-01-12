package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.txt_verti_reading_page.*

class TxtVertiReadingPage : ReadingPage() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("ReadingPage", "onCreate: Called")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.txt_verti_reading_page)

        addListener()
        addContent()
        super.startRecording()
    }

    override fun popupErrorMsg(msg: String?) {
        MyPopupWindow.showTextPopup(msg, this, R.id.txtVertiReadingPage) {
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }

    private fun addListener() {
        backButton.setOnClickListener {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(this.resources.getInteger(R.integer.vibrate_interval).toLong(),
                VibrationEffect.DEFAULT_AMPLITUDE))

            super.endRecording()
        }

        readingScrollView.setOnScrollChangeListener { _, scrollX, scrollY, oldScrollX, oldScrollY ->
            DataManager.printTxtVertiData(this, scrollX, scrollY, oldScrollX, oldScrollY)

            val start = textBody.layout.getLineStart(0)
            val end = textBody.layout.getLineEnd(textBody.lineCount - 1)

            Log.e("Start", "${textBody.layout.getLineStart(getFirstLineIndex())}")
            Log.e("End", "${textBody.layout.getLineEnd(getLastLineIndex())}")
            // Log.e("Displayed", textBody.text.toString().substring(start, end))
        }
    }

    private fun addContent() {
        textTitle.text = DataManager.mDataReader.mCachedInfo[DataManager.mNextFile]!!.fileTitle
        textBody.text = DataManager.mDataReader.mCachedBody[DataManager.mNextFile]
    }

    /**
     * Gets the first line that is visible on the screen.
     *
     * @return
     */
    fun getFirstLineIndex(): Int {
        return textBody.layout.getLineForVertical(readingScrollView.scrollY)
    }

    /**
     * Gets the last visible line number on the screen.
     * @return last line that is visible on the screen.
     */
    fun getLastLineIndex(): Int {
        return textBody.layout.getLineForVertical(readingScrollView.scrollY + readingScrollView.height)
    }
}

