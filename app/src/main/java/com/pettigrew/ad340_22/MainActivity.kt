package com.pettigrew.ad340_22

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class MainActivity : AppCompatActivity() {
    private var loginTextView: EditText? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null

    private var sharedPreferencesHelper: SharedPreferencesHelper? = null
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        From Assignment 1:
//        val textView = findViewById<TextView>(R.id.text)
//        textView.setText(R.string.bulleted_list)
//        Log.d("MainActivity", "Hello World")

         //Instantiate a SharedPreferencesHelper class
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        sharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences!!)


        // text entry fields
        loginTextView = findViewById(R.id.loginText)
        emailTextView = findViewById(R.id.emailText)
        passwordTextView = findViewById(R.id.passwordText)

        loginTextView!!.setText(sharedPreferencesHelper!!.getEntry("name"))
        emailTextView!!.setText(sharedPreferencesHelper!!.getEntry("email"))
        passwordTextView!!.setText(sharedPreferencesHelper!!.getEntry("password"))

        val mSignInButton = findViewById<Button>(R.id.loginButton)
        mSignInButton.setOnClickListener {
            Log.d("FIREBASE", "click")
            signIn()
        }
    }

    fun buttonSelected(button: View) {
        when (button.id) {
            R.id.movieButton -> {
                val intent = Intent(this, MovieActivity::class.java)
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
        }
    }

    private fun signIn() {
        Log.d("FIREBASE", "signIn")
        // 1 - validate display name, email, and password entries
        if (emailTextView!!.text.isBlank() || emailTextView!!.text == null || !emailTextView!!.text.contains(
                "@"
            ) || !emailTextView!!.text.contains(".")
        ) {
            Log.d("VALIDATION", "email not valid")
            //show error page or toast to user that they need to enter a valid email
            return
        }
        if (passwordTextView!!.text.isBlank() || passwordTextView!!.text == null || passwordTextView!!.text.length < 8 || !passwordTextView!!.text.contains(
                Regex("[a-z]")
            ) || !passwordTextView!!.text.contains(Regex("[0-9]"))
        ) {

            /*show error page or toast to user that they need to enter a valid password may need to break this out into specific case (empty/null compared to not meeting security requirements (length, numbers, letters)*/
            return
        }

        val login: String = loginTextView!!.text.toString()
        val email: String = emailTextView!!.text.toString()
        val password: String = passwordTextView!!.text.toString()


        // 2 - save valid entries to shared preferences

        sharedPreferencesHelper!!.saveEntry("name", login)
        sharedPreferencesHelper!!.saveEntry("email", email)
        sharedPreferencesHelper!!.saveEntry("password", password)

        // 3 - sign into Firebase
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
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
                                        this,
                                        FirebaseActivity::class.java
                                    )
                                )
                            }
                        }
                } else {
                    Log.d("FIREBASE", "sign-in failed")
                    Toast.makeText(
                        this, "Sign In Failed",
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