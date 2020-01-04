package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.VideoCapture
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_file_selection_page.*
import kotlinx.android.synthetic.main.content_file_selection_page.backButton

class FileSelectionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("FileSelectionPage", "onCreate: Called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_file_selection_page)

        addListener()
        addFiles()

        try {
            VideoCapture.init(this)
        }
        catch (e: Exception) {
            MyPopupWindow.showTextPopup(e.message, this, R.id.readingPage) {
                startActivity(Intent(this, FileSelectionPage::class.java))
            }
        }
    }

    private fun addListener() {
        backButton.setOnClickListener {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(this.resources.getInteger(R.integer.vibrate_interval).toLong(),
                VibrationEffect.DEFAULT_AMPLITUDE))

            if (DataManager.mParticipant.isSet()) {
                startActivity(Intent(this, AgreementPage::class.java))
            }
            else {
                startActivity(Intent(this, PersonalInfoPage::class.java))
            }
        }
    }

    private fun addFiles() {
        if (DataManager.mCachedFileDisplays.isEmpty()) {
            var fileNames = this.resources.assets.list("reading_files")

            if (fileNames != null) {
                for (fileName in fileNames) {
                    DataManager.mDataReader.readTxtByName(this, fileName)
                }
            }
        }

        fileRecyclerView.setHasFixedSize(true) // could use to improve performance if changes in content do not change the layout size of the RecyclerView
        fileRecyclerView.layoutManager = LinearLayoutManager(this)
        fileRecyclerView.adapter = FileAdapter(this)
    }
}
