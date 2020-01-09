package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xiugechen.reading_app.Data.DataManager
import com.xiugechen.reading_app.Data.ReadIndicator
import com.xiugechen.reading_app.R

class FileAdapter(val filePage: FileSelectionPage) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val fileView = LayoutInflater.from(parent.context).inflate(R.layout.content_file_selection_row, parent, false)

        return ViewHolder(fileView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileId = DataManager.mDataReader.mFileList[position]
        val fileInfo = DataManager.mDataReader.mCachedInfo[fileId] !!

        holder.filename.text = fileInfo.filename
        holder.fileDescription.text = fileInfo.fileDiscription

        val readIndicator = fileInfo.readIndicator

        when {
            readIndicator == ReadIndicator.UNREAD -> {
                holder.readIndicator.text = "unread"
                holder.readIndicator.setTextColor(Color.RED)
            }
            readIndicator == ReadIndicator.READ -> {
                holder.readIndicator.text = "read"
                holder.readIndicator.setTextColor(Color.GREEN)
            }
            else -> {
                holder.readIndicator.text = ""
            }
        }

        holder.readButton.setOnClickListener {
            val vibrator = filePage.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(filePage.resources.getInteger(R.integer.vibrate_interval).toLong(),
                VibrationEffect.DEFAULT_AMPLITUDE))

            fileInfo.readIndicator = ReadIndicator.READ
            DataManager.mNextFile = fileInfo.filename

            if (fileInfo.filename.contains(".txt")) {
                filePage.startActivity(Intent(filePage, TxtReadingPage::class.java))
            }
            else {
                filePage.startActivity(Intent(filePage, PdfReadingPage::class.java))
            }
        }
    }

    override fun getItemCount() : Int = DataManager.mDataReader.mFileList.size

    class ViewHolder(fileView: View) : RecyclerView.ViewHolder(fileView) {
        val filename: TextView = fileView.findViewById(R.id.filename)
        val fileDescription: TextView = fileView.findViewById(R.id.fileDescription)
        val readIndicator: TextView = fileView.findViewById(R.id.readIndicator)
        val readButton: Button = fileView.findViewById(R.id.readButton)
    }
}