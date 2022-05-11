package com.pettigrew.ad340_22

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/*Resources used:
    https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial#kotlin_1
    https://github.com/googlemaps/android-samples/blob/20a800d2b857775eb376083b6f291e939dcb677e/tutorials/kotlin/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.kt#L193-L223
*/


class TrafficMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myMap: GoogleMap
    private var locationPermissionGranted = false
    private var userLocation: Location? = null
    private val nsc = LatLng(47.6989479, -122.334865)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_maps)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Map of Seattle Traffic Cameras"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    Log.d("LOCATION", "success. Current location is " + userLocation.toString())
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        userLocation = task.result
                        if (userLocation != null) {
                            updateMap(userLocation)
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        val default = (Location(LocationManager.GPS_PROVIDER).apply {
                            latitude = nsc.latitude
                            longitude = nsc.longitude
                        })
                        updateMap(default)
                        myMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LOCATION", "previously granted")
            locationPermissionGranted = true
            getDeviceLocation()
        } else {
            Log.d("LOCATION", "need to request")
            requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                    getDeviceLocation()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }


    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                myMap.isMyLocationEnabled = true
                myMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                myMap.isMyLocationEnabled = false
                myMap.uiSettings.isMyLocationButtonEnabled = false
                userLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun updateMap(location: Location?) {
        Log.d("LOCATION", "updateMap")
        if (location != null) {
            Log.d("LOCATION", "move map to $location")
            myMap.apply {
                val position = LatLng(location.latitude, location.longitude)
                val geolocation = "Latitude: " + location.latitude + ", Longitude: " + location.longitude
                addMarker(
                    MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(geolocation)
                )
                // [START_EXCLUDE silent]
                moveCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM.toFloat()))
                // [END_EXCLUDE]
            }
            // load markers data
            TrafficCam.loadUrlData(this) { data ->
                for (i in 0 until data.size) {
                    myMap.apply {
                        val coordinates = LatLng(data[i].coordinates[0], data[i].coordinates[1])
                        val description = data[i].description
                        addMarker(
                            MarkerOptions()
                                .position(coordinates)
                                .title(description)
                        )
                    }
                }
            }
        }
    }


    companion object {
        private val TAG = TrafficMapsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 12
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    }
}