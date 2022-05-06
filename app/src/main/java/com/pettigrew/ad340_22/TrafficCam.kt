package com.pettigrew.ad340_22

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class TrafficCam(
    val coordinates: Array<Double>,
    val description: String,
    private val image: String,
    private val type: String
) {
    private val sdotCam = "http://www.seattle.gov/trafficcams/images/"
    private val wsdotCam = "http://images.wsdot.wa.gov/nw/"

    fun getImageUrl(): String {
        val baseURL = if (this.type == "sdot") {
            sdotCam
        } else {
            wsdotCam
        }
        return baseURL + this.image
    }

    companion object {
        var dataUrl = " https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"
        fun loadUrlData(context: Context, camList: (ArrayList<TrafficCam>) -> Unit) {
            val queue = Volley.newRequestQueue(context)
            val cameraList: ArrayList<TrafficCam> = ArrayList()
            val url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"

            //save the JSON object received from Volley request
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.d("JSONObject", "Response: %s".format(response.toString()))

                    //filter through JSON object to get camera values
                    val features = response.getJSONArray("Features")
                    //filtering individual points by referencing the 1 index that contains all points
                    for (pointObject in 1 until features.length()) {
                        val point = features.getJSONObject(pointObject)
                        val coordinates = point.getJSONArray("PointCoordinate")

//                        val camera = point.getJSONArray("Cameras").getJSONObject(0)
//                        val currentCamera = TrafficCam(
//                            arrayOf(coordinates.getDouble(0), coordinates.getDouble(1)),
//                            camera.getString("Description"),
//                            camera.getString("ImageUrl"),
//                            camera.getString("Type"),
//                        )
//                        cameraList.add(currentCamera)
                        //trying to figure out how to display multiple cameras, the below does not function without error
                        val cameras = point.getJSONArray("Cameras")
                        if (cameras.length() > 0) {
                            for (cameraObject in 0 until cameras.length()) {
                                val camera = cameras.getJSONObject(cameraObject)
                                val currentCamera = TrafficCam(
                                    arrayOf(
                                        coordinates.getDouble(0),
                                        coordinates.getDouble(1)
                                    ),
                                    camera.getString("Description"),
                                    camera.getString("ImageURL"),
                                    camera.getString("Type")
                                )
                                cameraList.add(currentCamera)
                            }
                        }
                        camList(cameraList)
                    }

                })
            { error ->
                Log.d(
                    "JSONObject",
                    "Failed to load JSON object from Volley. Error: %s".format(error.toString())
                )
            }
            queue.add(jsonObjectRequest)
        }
    }


}
