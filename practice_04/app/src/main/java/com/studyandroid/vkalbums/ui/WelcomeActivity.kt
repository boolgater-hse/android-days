package com.studyandroid.vkalbums.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.studyandroid.vkalbums.R
import com.studyandroid.vkalbums.databinding.ActivityWelcomeBinding
import com.studyandroid.vkalbums.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.welcomeOkayButton.setOnClickListener {
            val sharedPref =
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

            with(sharedPref.edit()) {
                putBoolean(getString(R.string.pref_is_first_time), false)
                commit()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
