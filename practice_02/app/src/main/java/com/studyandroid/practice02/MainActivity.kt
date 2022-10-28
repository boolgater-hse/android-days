package com.studyandroid.practice02

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    var flagKeyboardOnScreen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout_main)
        constraintLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            constraintLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight: Int = constraintLayout.rootView.height
            val keypadHeight: Int = screenHeight - rect.bottom
            if (keypadHeight > screenHeight * 0.15) {
                if (!flagKeyboardOnScreen) {
                    flagKeyboardOnScreen = true
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.toast_keyboard_appeared_event),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (flagKeyboardOnScreen) {
                    flagKeyboardOnScreen = false
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.toast_keyboard_hidden_event),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Toast.makeText(
                    this,
                    getString(R.string.toast_landscape_event),
                    Toast.LENGTH_SHORT
                ).show()
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                Toast.makeText(
                    this,
                    getString(R.string.toast_portrait_event),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
        super.onConfigurationChanged(newConfig)
    }
}
