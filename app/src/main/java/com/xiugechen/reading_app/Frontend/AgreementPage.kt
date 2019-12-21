package com.xiugechen.reading_app.Frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.MainActivity
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_agreement_page.*

class AgreementPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AgreementPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_page)

        addListener()

        setAgreementText()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        nextButton.setOnClickListener {
            Log.d("test", "To do")
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
        agreementText.setText("Agreement test \n test 1 \n test 2 \n test 3 \n \n \n \n " +
                "\n \n \n \n test 4 \n \n \n \n test 5 \n \n \n \n \n test 6 \n \n \n \n" +
                "test 6 \n \n \n \n test 7 \n \n \n \n test 8 \n \n \n \n test 9 \n \n \n \n" +
                "test 10")
    }
}