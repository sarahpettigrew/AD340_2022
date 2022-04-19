package com.pettigrew.ad340_22

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        From Assignment 1:
//        val textView = findViewById<TextView>(R.id.text)
//        textView.setText(R.string.bulleted_list)
//        Log.d("MainActivity", "Hello World")

    }
    fun sendMessage(view: View) {
        // Do something in response to button click
        val text = "test"
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(applicationContext, text, duration).show()
    }
}