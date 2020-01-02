package com.xiugechen.reading_app.Data

import android.media.MediaRecorder
import java.lang.Exception

object VideoCapture {
    var isRecording = true
    var mediaRecorder = MediaRecorder()

    fun StartRecord_FrontCamera() {
        if (isRecording) {
            throw Exception("Front Camera already in use, please try again later")
        }
        else {
            
            isRecording = true
        }
    }

    fun EndRecordAndSave_FrontCamera() {
        if (isRecording) {

            isRecording = false
        }
        else {
            throw Exception("Front Camera not in use, please try again later")
        }
    }
}