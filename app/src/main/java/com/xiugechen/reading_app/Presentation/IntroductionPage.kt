package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.MainActivity
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_introduction_page.*

class IntroductionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("IntroductionPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_introduction_page)

        addListener()

        setIntroductionText()
    }

    private fun addListener() {
        nextButton.setOnClickListener {
            startActivity(Intent(this, AgreementPage::class.java))
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setIntroductionText() {
        val readText = DataManager.mDataReader.readTxt(this, R.raw._introduction)
        introductionTextTitle.text = readText.first
        introductionTextBody.text = readText.third
    }
}