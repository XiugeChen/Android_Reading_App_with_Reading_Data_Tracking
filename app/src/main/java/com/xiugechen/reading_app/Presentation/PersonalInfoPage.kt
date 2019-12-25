package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.Participant
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_personal_info_page.*

class PersonalInfoPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("PersonalInfoPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info_page)

        addListener()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, AgreementPage::class.java))
        }

        nextButton.setOnClickListener {
            val fullname: String = nameInputText.text.toString()
            val genders = resources.getStringArray(R.array.gender_arrays)
            val gender = genders[genderSpinner.selectedItemPosition]

            DataManager.participant = Participant(fullname, gender)

            startActivity(Intent(this, FileSelectionPage::class.java))
        }
    }
}