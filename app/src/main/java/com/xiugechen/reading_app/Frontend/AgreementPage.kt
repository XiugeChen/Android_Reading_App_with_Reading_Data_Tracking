package com.xiugechen.reading_app.Frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.MainActivity
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_agreement_page.*

class AgreementPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_page)

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}