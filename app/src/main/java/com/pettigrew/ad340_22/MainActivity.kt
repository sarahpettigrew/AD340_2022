package com.pettigrew.ad340_22

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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

    fun buttonSelected(button : View){
        if(button.id == R.id.movieButton){
            val intent =  Intent(this, MovieActivity::class.java)
            startActivity(intent)
        }
        else if(button.id == R.id.trafficButton){
            val intent = Intent(this, TrafficActivity::class.java)
            startActivity(intent)
        }
    }

    fun sendMessage(button: View) {
        // Do something in response to button click
        val b = button as Button
        val buttonText = b.text.toString()
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(applicationContext, buttonText, duration).show()
    }
}