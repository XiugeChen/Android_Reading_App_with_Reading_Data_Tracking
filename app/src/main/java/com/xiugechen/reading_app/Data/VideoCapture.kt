package com.xiugechen.reading_app.Data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Camera
import android.hardware.camera2.*
import android.media.CamcorderProfile
import android.media.MediaDataSource
import android.media.MediaRecorder
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest

object VideoCapture {
    private var isRecording = false

    /**
     * Orientation of the camera sensor
     */
    private var sensorOrientation = 0

    /**
     * The [android.util.Size] of video recording.
     */
    private lateinit var videoSize: Size

    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private val cameraOpenCloseLock = Semaphore(1)

    /**
     * Media recorder
     */
    private var mMediaRecorder: MediaRecorder? = null

    /**
     * A reference to the opened [android.hardware.camera2.CameraDevice].
     */
    private var mCameraDevice: CameraDevice? = null

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private var backgroundThread: HandlerThread? = null

    /**
     * A [Handler] for running tasks in the background.
     */
    private var backgroundHandler: Handler? = null

    /**
     * Parameters for set sensorOrientation of [MediaRecorder]
     */
    private val SENSOR_ORIENTATION_DEFAULT_DEGREES = 90
    private val SENSOR_ORIENTATION_INVERSE_DEGREES = 270
    private val DEFAULT_ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }
    private val INVERSE_ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 270)
        append(Surface.ROTATION_90, 180)
        append(Surface.ROTATION_180, 90)
        append(Surface.ROTATION_270, 0)
    }


    fun init(appActivity: AppCompatActivity) {
        openCamera(appActivity)
        startBackgroundThread()
    }

    fun StartRecord_FrontCamera(appActivity: AppCompatActivity) {
        if (isRecording) {
            throw RuntimeException("Front Camera already in use, please try again later")
        }

        if (mCameraDevice == null || mMediaRecorder == null) {
            openCamera(appActivity)
            throw RuntimeException("Front Camera not open, please try again later")
        }

        if (backgroundThread == null || backgroundHandler == null) {
            startBackgroundThread()
            throw RuntimeException("Front Camera background thread not set, please try again later")
        }

        setUpMediaRecorder(appActivity)

        // Set up Surface for MediaRecorder
        val recorderSurface = mMediaRecorder!!.surface
        val surfaces = ArrayList<Surface>().apply {
            add(recorderSurface)
        }

        // Start a capture session
        // Once the session starts, we can update the UI and start recording
        mCameraDevice?.createCaptureSession(surfaces,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    throw RuntimeException("Camera capture session config failed")
                }

                override fun onConfigured(p0: CameraCaptureSession) {
                }

                override fun onReady(session: CameraCaptureSession) {
                    super.onReady(session)

                    try {
                        val builder: CaptureRequest.Builder? = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                        builder?.addTarget(mMediaRecorder?.surface!!)
                        val request = builder!!.build()
                        session.setRepeatingRequest(request, null, backgroundHandler)

                        appActivity.runOnUiThread {
                            isRecording = true
                            mMediaRecorder?.start()
                        }
                    } catch (e: Exception) {

                    }
                }

            }, backgroundHandler)

        Log.i("VideoCapture", "Camera start successfully")
    }

    fun EndRecordAndSave_FrontCamera() {
        if (!this.isRecording) {
            throw RuntimeException("Front Camera not in use, please try again later")
        }

        try {
            this.mMediaRecorder?.apply {
                stop()
                reset()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        closeCamera()

        stopBackgroundThread()

        this.isRecording = false
        Log.i("VideoCapture", "Camera record end successfully")
    }

    /**
     * Close the [CameraDevice].
     */
    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            mCameraDevice?.close()
            mCameraDevice = null
            mMediaRecorder?.release()
            mMediaRecorder = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    /**
     * Tries to open a [CameraDevice]. The result is listened by [stateCallback].
     */
    private fun openCamera(appActivity: AppCompatActivity) {
        appActivity.checkSelfPermission(android.Manifest.permission.CAMERA)
        appActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        appActivity.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)

        val manager = appActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
            throw RuntimeException("Time out waiting to lock camera opening.")
        }

        if (manager.cameraIdList.size < 2) {
            throw RuntimeException("Front camera not available")
        }

        val cameraId = manager.cameraIdList[1]
        // Choose the sizes for video recording
        val characteristics = manager.getCameraCharacteristics(cameraId)
        val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?:
        throw RuntimeException("Cannot get available preview/video sizes")

        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        videoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder::class.java))

        mMediaRecorder = MediaRecorder()
        manager.openCamera(cameraId, stateCallback, backgroundHandler)
    }

    /**
     * Set up the media recorder
     */
    private fun setUpMediaRecorder(appActivity: AppCompatActivity) {
        val rotation = appActivity.windowManager.defaultDisplay.rotation
        when (sensorOrientation) {
            SENSOR_ORIENTATION_DEFAULT_DEGREES ->
                mMediaRecorder?.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation))
            SENSOR_ORIENTATION_INVERSE_DEGREES ->
                mMediaRecorder?.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation))
        }

        mMediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(getVideoFilePath(appActivity))
            setVideoEncodingBitRate(10000000)
            setVideoFrameRate(30)
            setVideoSize(videoSize.width, videoSize.height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
        }
    }

    /**
     * Starts a background thread and its [Handler].
     */
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("VideoCapBackground")
        backgroundThread?.start()
        backgroundHandler = Handler(backgroundThread?.looper)
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        backgroundThread?.join()
        backgroundThread = null
        backgroundHandler = null
    }

    /**
     * [CameraDevice.StateCallback] is called when [CameraDevice] changes its status.
     */
    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            mCameraDevice = cameraDevice
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            mCameraDevice?.close()
            throw RuntimeException("Camera Disconnected")
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraOpenCloseLock.release()
            mCameraDevice?.close()
            throw RuntimeException("Camera Error")
        }
    }

    /**
     * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private fun chooseVideoSize(choices: Array<Size>) = choices.firstOrNull {
        it.width == it.height * 4 / 3 && it.width <= 1080 } ?: choices[choices.size - 1]

    /**
     * Get the external video storage path
     */
    private fun getVideoFilePath(context: Context?): String {
        // val filename = "${System.currentTimeMillis()}.mp4"
        val filename = "test.mp4"
        val dir = context?.getExternalFilesDir(null)

        return if (dir == null) {
            filename
        } else {
            "${dir.absolutePath}/$filename"
        }
    }
}