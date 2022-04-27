package com.pettigrew.ad340_22

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val movieList = findViewById<ListView>(R.id.movies)

        title = "Movies w/ List View"
        val adapter = MovieAdapter(this, Movies.movies)
        movieList.adapter = adapter
    }

    class MovieAdapter(private val context: Context, private val values: Array<Array<String>>) : BaseAdapter() {
        private lateinit var movieTitle: TextView
        private lateinit var movieYear: TextView
        private lateinit var movieImage: ImageView
        override fun getCount(): Int {
            return values.size
        }
        override fun getItem(position: Int): Any {
            return position
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context
                .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.movie_row_layout, parent, false)
            movieTitle = rowView.findViewById(R.id.movieTitle)
            movieYear = rowView.findViewById(R.id.movieYear)
            movieImage = rowView.findViewById(R.id.movieImage)

            rowView.setOnClickListener {
                Toast.makeText(rowView.context, Movies.movies[position][0], Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(context, MovieDetailActivity::class.java)
                val b = Bundle()
                b.putStringArray("movie details", Movies.movies[position])
                intent.putExtras(b)
                context.startActivity(intent)
            }

            movieTitle.text = values[position][0]
            movieYear.text = values[position][1]
            Picasso.get().setIndicatorsEnabled(true)
            Picasso.get().load(values[position][3]).into(movieImage)
            return rowView
        }
    }
}