package com.pettigrew.ad340_22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Movie Details"
        val b = this.intent.extras
        val movieData = b?.getStringArray("movie details")

        val imageView = findViewById<ImageView>(R.id.movieImageLarge)
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val movieDir = findViewById<TextView>(R.id.movieDirector)
        val movieYear = findViewById<TextView>(R.id.movieYear)
        val movieDesc = findViewById<TextView>(R.id.movieDesc)

        if (movieData !== null) {
            movieTitle.text = movieData[0]
            movieYear.text = movieData[1]
            movieDir.text = movieData[2]
            movieDesc.text = movieData[4]
            Picasso.get().load(movieData[3]).into(imageView)
        }
    }
}