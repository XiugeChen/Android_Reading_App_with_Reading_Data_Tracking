package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.Participant
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_personal_info_page.*

class PersonalInfoPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("PersonalInfoPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_personal_info_page)

        addListener()
    }

    private fun addListener() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrate_interval = this.resources.getInteger(R.integer.vibrate_interval).toLong()

        backButton.setOnClickListener {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrate_interval, VibrationEffect.DEFAULT_AMPLITUDE))
            startActivity(Intent(this, AgreementPage::class.java))
        }

        nextButton.setOnClickListener {
            val fullname: String = nameInputText.text.toString().trim()

            if (fullname.isEmpty()) {
                Toast.makeText(this,R.string.name_miss_msg, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val genders = resources.getStringArray(R.array.gender_arrays)
            val gender = genders[genderSpinner.selectedItemPosition]

            DataManager.mParticipant = Participant(fullname, gender)
            DataManager.printData()

            vibrator.vibrate(VibrationEffect.createOneShot(vibrate_interval, VibrationEffect.DEFAULT_AMPLITUDE))
            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }
}