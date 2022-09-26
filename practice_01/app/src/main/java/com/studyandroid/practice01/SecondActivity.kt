package com.studyandroid.practice01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val buttonBack = findViewById<Button>(R.id.second_back_button)
        buttonBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.second_back_button -> {
                val intent = Intent()
                intent.putExtra("one", findViewById<EditText>(R.id.second_one_text).text.toString())
                intent.putExtra("two", findViewById<EditText>(R.id.second_two_text).text.toString())
                intent.putExtra("url", findViewById<EditText>(R.id.second_url_text).text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}
