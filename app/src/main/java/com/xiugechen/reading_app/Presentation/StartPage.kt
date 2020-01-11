package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.xiugechen.reading_app.Data.Config
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.R

import kotlinx.android.synthetic.main.start_page.*

class StartPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "onCreate: Called")
        super.onCreate(savedInstanceState)

        if (Config.isBlackMode) {
            setTheme(R.style.DarkTheme)
        }
        else {
            setTheme(R.style.LightTheme)
        }
        setContentView(R.layout.start_page)

        addListener()
    }

    /**
     * Global init point, init all singleton object
     */
    override fun onStart() {
        super.onStart()
        DataManager.mDataReader.init(this)
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
        startButton.setOnClickListener {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(this.resources.getInteger(
                R.integer.vibrate_interval
            ).toLong(),
                VibrationEffect.DEFAULT_AMPLITUDE))
            startActivity(Intent(this, IntroductionPage::class.java))
        }
    }
}
