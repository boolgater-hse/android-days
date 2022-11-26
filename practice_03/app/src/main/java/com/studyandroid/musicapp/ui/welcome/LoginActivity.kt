package com.studyandroid.musicapp.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = getSharedPreferences(
            getString(R.string.pref_current_user), Context.MODE_PRIVATE
        )
        val currentUser = sharedPref.getString(getString(R.string.pref_current_user), "")
        if (!currentUser.isNullOrBlank()) {
            Log.d("LoginActivity", "Logged in as $currentUser")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this,
                String.format(getString(R.string.main_logged_in_as), currentUser),
                Toast.LENGTH_SHORT
            ).show()
            finishAffinity()
        }
    }
}
