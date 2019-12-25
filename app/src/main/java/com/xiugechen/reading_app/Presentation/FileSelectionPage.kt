package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.activity_file_selection_page.*

class FileSelectionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("FileSelectionPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_selection_page)

        addListener()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            startActivity(Intent(this, PersonalInfoPage::class.java))
        }
    }
}