package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_agreement_page.*

class AgreementPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("AgreementPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_page)

        addListener()

        setAgreementText()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, IntroductionPage::class.java))
        }

        nextButton.setOnClickListener {
            startActivity(Intent(this, PersonalInfoPage::class.java))
        }

        agreementCheckBox.setOnClickListener {
            if (nextButton.isEnabled) {
                nextButton.setEnabled(false)
            }
            else {
                nextButton.setEnabled(true)
            }
        }
    }

    private fun setAgreementText() {
        agreementText.setText(DataManager.dataReader.readTxt(this, R.raw._agreement))
    }
}