package com.studyandroid.practice01

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FirstActivity : AppCompatActivity(), View.OnClickListener {
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val oneText: String? = result.data?.getStringExtra("one")
                val twoText: String? = result.data?.getStringExtra("two")
                val urlText: String? = result.data?.getStringExtra("url")
                runToast(oneText)
                runToast(twoText)
                if (!urlText.isNullOrBlank()) {
                    runOpenURLAskingDialog(urlText)
                }
            }
        }

    private fun runToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (!text.isNullOrBlank()) {
            Toast.makeText(this, text, duration).show()
        }
    }

    private fun runOpenURLAskingDialog(url: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Open site")
            setMessage("Really want to open this site?\n\n$url")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Open") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(
                    Intent.ACTION_VIEW, (
                            if (Uri.parse(url).scheme.isNullOrBlank())
                                Uri.parse("http://$url")
                            else
                                Uri.parse(url)
                            )
                )
                startActivity(intent)
            }
        }.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val buttonGoSecond = findViewById<Button>(R.id.main_button)
        buttonGoSecond.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.main_button -> {
                val intent = Intent(this, SecondActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
    }
}
