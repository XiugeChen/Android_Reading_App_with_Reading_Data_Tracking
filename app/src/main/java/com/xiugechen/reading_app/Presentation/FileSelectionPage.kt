package com.xiugechen.reading_app.Presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.FileDisplay
import com.xiugechen.reading_app.Data.ReadIndicator
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.content_file_selection_page.*

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
        if (DataManager.mCachedFileDisplays.isEmpty()) {
            var fileNames = this.resources.assets.list("reading_files")

            if (fileNames != null) {
                for (fileName in fileNames) {
                    DataManager.mCachedFileDisplays.add(FileDisplay(fileName,
                        "description", ReadIndicator.UNREAD))
                }
            }
            else {
                DataManager.mCachedFileDisplays.add(FileDisplay("No files were found",
                    "please try it again", ReadIndicator.UNKOWN))
            }
        }

        fileRecyclerView.setHasFixedSize(true) // could use to improve performance if changes in content do not change the layout size of the RecyclerView
        fileRecyclerView.layoutManager = LinearLayoutManager(this)
        fileRecyclerView.adapter = FileAdapter()
    }
}
