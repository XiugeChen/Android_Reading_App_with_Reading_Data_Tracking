package com.xiugechen.reading_app.Presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.xiugechen.reading_app.R

object MyPopupWindow {
    private const val WIDTH = LinearLayout.LayoutParams.MATCH_PARENT
    private const val HEIGHT = LinearLayout.LayoutParams.MATCH_PARENT
    private const val FOCUSABLE = true

    /**
     * Pop up new window from appActivity, with message textDisplay
     */
    @SuppressLint("InflateParams")
    fun showTextPopup(textToDisplay: String?, appActivity: Activity, appLayoutId: Int,
                      closeFun: () -> Unit) {

        val inflater: LayoutInflater = appActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater

        val popupView = inflater.inflate(R.layout.popup_window, null)

        val popupWindow = PopupWindow(popupView, WIDTH, HEIGHT, FOCUSABLE)

        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }

        // If API level 23 or higher then execute the code create a new slide animation for
        // popup window enter/exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut
        }

        val mPopupText = popupView.findViewById<TextView>(R.id.popupText)
        val closeButton = popupView.findViewById<Button>(R.id.closeButton)

        mPopupText.text = textToDisplay ?: ""

        closeButton.setOnClickListener {
            val vibrator = appActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(
                VibrationEffect.createOneShot(appActivity.resources.getInteger(R.integer.vibrate_interval).toLong(),
                    VibrationEffect.DEFAULT_AMPLITUDE))

            popupWindow.dismiss()
            closeFun()
        }

        // Show the popup window on app only after all the lifecycle methods are called
        val parentLayout = appActivity.findViewById<ViewGroup>(appLayoutId)

        TransitionManager.beginDelayedTransition(parentLayout)
        parentLayout.post {
            popupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0, 0)
        }
    }
}