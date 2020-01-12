package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.pdf_reading_page.*

class PdfReadingPage : ReadingPage(), OnPageScrollListener, OnPageChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("PdfReadingPage", "onCreate: Called")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.pdf_reading_page)

        addListener()
        addContent()
        startRecording()
    }

    override fun popupErrorMsg(msg: String?) {
        MyPopupWindow.showTextPopup(msg, this, R.id.pdfReadingPage) {
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }

    override fun onPageScrolled(page: Int, positionOffset: Float) {
        if (!Config.isHorizontallySwipe)
            DataManager.printPdfVertiData(this, page, positionOffset)
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        if (Config.isHorizontallySwipe)
            DataManager.printHoriData(this, page + 1, pageCount, false)
    }

    private fun addListener() {
        backButton.setOnClickListener {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(
                VibrationEffect.createOneShot(this.resources.getInteger(R.integer.vibrate_interval).toLong(),
                    VibrationEffect.DEFAULT_AMPLITUDE))

            super.endRecording()
        }
    }

    private fun addContent() {
        textTitle.text = DataManager.mDataReader.mCachedInfo[DataManager.mNextFile]!!.fileTitle

        val filePath = DataManager.mDataReader.mCachedBody[DataManager.mNextFile]!!

        readingScrollView.fromAsset(filePath)
            .swipeHorizontal(Config.isHorizontallySwipe)
            .onPageScroll(this)
            .onPageChange(this)
            .enableDoubletap(true)
            .pageFitPolicy(FitPolicy.WIDTH)
            .nightMode(Config.isBlackMode)
            .load()
    }
}