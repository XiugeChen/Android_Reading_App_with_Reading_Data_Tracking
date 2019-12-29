package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.graphics.text.LineBreaker
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_reading_page.*

class ReadingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("PersonalInfoPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_reading_page)

        addListener()
        addContent()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }

    private fun addContent() {
        textTitle.text = DataManager.mNextFileDisplay.fileTitle
        textBody.text = DataManager.mNextFileDisplay.fileContent
    }
}

