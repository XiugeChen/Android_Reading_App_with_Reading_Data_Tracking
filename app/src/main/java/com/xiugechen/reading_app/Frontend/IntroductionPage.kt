package com.xiugechen.reading_app.Frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.MainActivity
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_introduction_page.*

class IntroductionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("IntroductionPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction_page)

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
        introductionText.setText("Introduction test \n test 1 \n test 2 \n test 3 \n \n \n \n " +
                "\n \n \n \n test 4 \n \n \n \n test 5 \n \n \n \n \n test 6 \n \n \n \n" +
                "test 6 \n \n \n \n test 7 \n \n \n \n test 8 \n \n \n \n test 9 \n \n \n \n" +
                "test 10")
    }
}