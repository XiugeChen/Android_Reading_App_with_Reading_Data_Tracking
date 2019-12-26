package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_file_selection_page.*
import java.io.InputStream

class FileSelectionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("FileSelectionPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_file_selection_page)

        addListener()

        addFiles()
    }

    private fun addListener() {
        backButton.setOnClickListener {
            if (DataManager.mParticipant.isSet()) {
                startActivity(Intent(this, AgreementPage::class.java))
            }
            else {
                startActivity(Intent(this, PersonalInfoPage::class.java))
            }
        }
    }

    private fun addFiles() {
        var lists = this.resources.assets.list("reading_files")

        if (lists != null) {
            for (list in lists) {
                Log.d("1", list)
            }
        }
    }
}
