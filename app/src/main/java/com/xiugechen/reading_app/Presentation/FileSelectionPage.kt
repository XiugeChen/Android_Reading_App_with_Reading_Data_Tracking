package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.REQUEST_VIDEO_PERMISSIONS
import com.xiugechen.reading_app.Data.VideoCapture
import com.xiugechen.reading_app.R
import kotlinx.android.synthetic.main.file_selection_page.*
import kotlinx.android.synthetic.main.file_selection_page.backButton

class FileSelectionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("FileSelectionPage", "onCreate: Called")
        super.onCreate(savedInstanceState)

        if (Config.isBlackMode) {
            setTheme(R.style.DarkTheme)
        }
        else {
            setTheme(R.style.LightTheme)
        }
        setContentView(R.layout.file_selection_page)

        addListener()
        addFiles()

        try {
            VideoCapture.init(this)
        }
        catch (e: Exception) {
            popupErrorMsg(e.message)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_VIDEO_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission granted
                    try {
                        VideoCapture.init(this)
                    }
                    catch (e: Exception) {
                        popupErrorMsg(e.message)
                    }
                } else {
                    // permission denied
                    popupErrorMsg("This app need video and audio permission to run, please open them in system settings")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            Config.isBlackMode = !Config.isBlackMode

            finish()
            startActivity(intent)

            return true
        }

        return super.onOptionsItemSelected(item)
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
        if (DataManager.mDataReader.mFileList.isEmpty()) {
            var fileNames = this.resources.assets.list("reading_files")

            if (fileNames != null) {
                for (fileName in fileNames) {
                    DataManager.mDataReader.getReadingFile(this, fileName)
                }
            }
        }

        fileRecyclerView.setHasFixedSize(true) // could use to improve performance if changes in content do not change the layout size of the RecyclerView
        fileRecyclerView.layoutManager = LinearLayoutManager(this)
        fileRecyclerView.adapter = FileAdapter(this)
    }

    private fun popupErrorMsg(msg: String?) {
        MyPopupWindow.showTextPopup(msg, this, R.id.fileSelectionPage) {
            if (DataManager.mParticipant.isSet()) {
                startActivity(Intent(this, AgreementPage::class.java))
            }
            else {
                startActivity(Intent(this, PersonalInfoPage::class.java))
            }
        }
    }
}
