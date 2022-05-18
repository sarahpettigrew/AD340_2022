package com.pettigrew.ad340_22

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


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
        when (button.id) {
            R.id.movieButton -> {
                val intent =  Intent(this, MovieActivity::class.java)
                startActivity(intent)
            }
            R.id.trafficButton -> {
                val intent = Intent(this, TrafficActivity::class.java)
                startActivity(intent)
            }
            R.id.mapsButton -> {
                val intent = Intent(this, TrafficMapsActivity::class.java)
                startActivity(intent)
            }
            R.id.loginButton ->{
                //call sign in function
            }
        }
    }
    private fun signIn(login : String, email : String, password : String) {
        Log.d("FIREBASE", "signIn")

        // 1 - validate display name, email, and password entries


        // 2 - save valid entries to shared preferences


        // 3 - sign into Firebase
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    // update profile. displayname is the value entered in UI
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(login)
                        .build()
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { event ->
                            if (event.isSuccessful) {
                                Log.d("FIREBASE", "User profile updated.")
                                // Go to FirebaseActivity
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        FirebaseActivity::class.java
                                    )
                                )
                            }
                        }
                } else {
                    Log.d("FIREBASE", "sign-in failed")
                    Toast.makeText(
                        this@MainActivity, "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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