package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_agreement_page.*

class AgreementPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("AgreementPage", "onCreate: Called")
        super.onCreate(savedInstanceState)

        if (Config.isBlackMode) {
            setTheme(R.style.DarkTheme)
        }
        else {
            setTheme(R.style.LightTheme)
        }
        setContentView(R.layout.content_agreement_page)

        addListener()

        setAgreementText()
    }

    private fun addListener() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrate_interval = this.resources.getInteger(R.integer.vibrate_interval).toLong()

        backButton.setOnClickListener {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrate_interval, VibrationEffect.DEFAULT_AMPLITUDE))
            startActivity(Intent(this, IntroductionPage::class.java))
        }

        nextButton.setOnClickListener {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrate_interval, VibrationEffect.DEFAULT_AMPLITUDE))

            if (DataManager.mParticipant.isSet()) {
                startActivity(Intent(this, FileSelectionPage::class.java))
            }
            else {
                startActivity(Intent(this, PersonalInfoPage::class.java))
            }
        }

        agreementCheckBox.setOnClickListener {
            nextButton.isEnabled = !nextButton.isEnabled

            if (nextButton.isEnabled && Config.isBlackMode) {
                nextButton.setTextColor(backButton.currentTextColor)
            }

            if (!nextButton.isEnabled) {
                nextButton.setTextColor(resources.getColor(R.color.light_gray, this.theme))
            }
        }
    }

    private fun setAgreementText() {
        val readText = DataManager.mDataReader.mAgreement
        agreementTextTitle.text = readText.first
        agreementTextBody.text = readText.second
    }
}