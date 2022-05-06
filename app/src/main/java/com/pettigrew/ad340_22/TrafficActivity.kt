package com.pettigrew.ad340_22

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.pettigrew.ad340_22.TrafficActivity.TrafficAdapter
import com.squareup.picasso.Picasso

class TrafficActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //check if network connected
        //References: https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin

        val connMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val currentNetwork = connMgr.activeNetwork
        if (currentNetwork != null) {
            val capabilities =
                connMgr.getNetworkCapabilities(connMgr.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    }
                }

                TrafficCam.loadUrlData(this) { data ->
                    val cameraList = findViewById<ListView>(R.id.cameras)
                    val listAdapter = TrafficAdapter(data)
                    cameraList?.adapter = listAdapter
                }
            }
            } else {
                Log.i("Internet", "NetworkCapabilities.NONE")
            }
        }
    inner class TrafficAdapter(private val values: List<TrafficCam>) : ArrayAdapter<TrafficCam?>(applicationContext, 0, values) {
        private lateinit var description: TextView
        private lateinit var image: ImageView
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context
                .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.camera_row_layout, parent, false)
            description = rowView.findViewById(R.id.cameraDesc)
            image = rowView.findViewById(R.id.cameraImg)

            description.text = values[position].description
            val imgURL = values[position].getImageUrl()
            Picasso.get().setIndicatorsEnabled(true)

            if(imgURL.isNotEmpty()){
                Picasso.get().load(imgURL).into(image)
            }
            return rowView
        }
    }
}