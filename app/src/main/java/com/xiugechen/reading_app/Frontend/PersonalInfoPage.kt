package com.xiugechen.reading_app.Frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_personal_info_page.*

class PersonalInfoPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("PersonalInfoPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info_page)

        addListener()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, AgreementPage::class.java))
        }

        nextButton.setOnClickListener {
            Log.d("PersonalInfoPage", "Todo")
        }
    }
}