package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_agreement_page.*

class AgreementPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("AgreementPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_agreement_page)

        addListener()

        setAgreementText()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, IntroductionPage::class.java))
        }

        nextButton.setOnClickListener {
            if (DataManager.mParticipant.isSet()) {
                startActivity(Intent(this, FileSelectionPage::class.java))
            }
            else {
                startActivity(Intent(this, PersonalInfoPage::class.java))
            }
        }

        agreementCheckBox.setOnClickListener {
            nextButton.isEnabled = !nextButton.isEnabled
        }
    }

    private fun setAgreementText() {
        val readText = DataManager.mDataReader.readTxtById(this, R.raw._agreement)
        agreementTextTitle.text = readText.first
        agreementTextBody.text = readText.third
    }
}