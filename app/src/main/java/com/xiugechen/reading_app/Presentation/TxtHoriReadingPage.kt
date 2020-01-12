package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.txt_hori_reading_page.*

class TxtHoriReadingPage : ReadingPage() {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    private var dataList = ArrayList<String>()

    inner class PageChangeCallback(val activity: TxtHoriReadingPage): ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            activity.onPageSelected(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("TxtHoriReadingPage", "onCreate: Called")
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
        setContentView(R.layout.txt_hori_reading_page)

        loadText() // must do it before set adapter
        addListener()
        addContent()

        viewPager = findViewById(R.id.readingViewPager)

        val pagerAdapter = TxtHoriReadingViewPagerAdapter(this, dataList, viewPager)
        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(PageChangeCallback(this))

        super.startRecording()
    }

    override fun popupErrorMsg(msg: String?) {
        MyPopupWindow.showTextPopup(msg, this, R.id.txtHoriReadingPage) {
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }

    private fun onPageSelected(page: Int) {
        DataManager.printHoriData(this, page + 1, dataList.size, true)
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
    }

    private fun loadText() {
        dataList.add(DataManager.mDataReader.mCachedBody[DataManager.mNextFile].toString())
        dataList.add(DataManager.mDataReader.mCachedBody[DataManager.mNextFile].toString())
        dataList.add(DataManager.mDataReader.mCachedBody[DataManager.mNextFile].toString())
    }
}