package com.pettigrew.ad340_22

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class FirebaseActivity : AppCompatActivity() {
    private var myRef: DatabaseReference? = null
    var userList: ListView? = null
    private var listAdapter: UserListAdapter? = null
    var userData = ArrayList<User?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)
        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Firebase"
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        userList = findViewById(R.id.userList)
        listAdapter = UserListAdapter(this, userData)
        userList?.adapter = listAdapter
        val mDatabase = FirebaseDatabase.getInstance()
        myRef = mDatabase.getReference("users")
        val members = myRef!!.orderByChild("updated")
        assert(currentUser != null)
        writeNewUser(currentUser!!.uid, currentUser.displayName, currentUser.email)

        // get list of users
        members.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("FIREBASE", "onDataChange")
                for (userSnapshot in dataSnapshot.children) {
                    val username = userSnapshot.child("username").value
                    if (username != null) {
                        val user = User(
                            username.toString(),
                            userSnapshot.child("email").value.toString(),
                            userSnapshot.child("updated").value.toString()
                        )
                        userData.add(user)
                    }
                }
                // Firebase doesn't support sort in descending order. Have to reverse in java
                userData.reverse()
                listAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadUsers:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    private fun writeNewUser(userId: String, name: String?, email: String?) {
        val user = User(name, email, Date().toString())
        myRef!!.child(userId).setValue(user)
    }

    @IgnoreExtraProperties
    class User {
        var username: String? = null
        var email: String? = null
        var updated: String? = null

        constructor() {
            /* Default constructor required for calls to DataSnapshot.getValue(User.class) */
        }

        constructor(username: String?, email: String?, updated: String) {
            this.username = username
            this.email = email
            this.updated = updated
        }
    }

    private class UserListAdapter(
        context : Context,
        private val values: ArrayList<User?>
    ) :
        ArrayAdapter<User?>(context, 0, values) {
        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context
                .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.list_item, parent, false)
            val title = rowView.findViewById<TextView>(R.id.item_title)
            title.text = values[position]!!.username
            val subtitle = rowView.findViewById<TextView>(R.id.item_subtitle)
            subtitle.text = String.format("Updated: %s", values[position]!!.updated)
            return rowView
        }
    }

    companion object {
        private val TAG = FirebaseActivity::class.java.simpleName
    }
}