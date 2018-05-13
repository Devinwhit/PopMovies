package com.devinwhitney.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinwhitney.android.popularmovies.model.MovieInformation;
import com.squareup.picasso.Picasso;


/**
 * Created by devin on 5/9/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private ImageView mMoviePoster;
    private ImageView mFullPoster;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        MovieInformation movieInformation = intent.getParcelableExtra("data");


        mMoviePoster = findViewById(R.id.movie_poster);
        mFullPoster = findViewById(R.id.full_poster);
        mRating = findViewById(R.id.rating);
        mReleaseDate = findViewById(R.id.release_date);
        mPlot = findViewById(R.id.plot);
        initUI(movieInformation);
    }

    private void initUI(MovieInformation movieInformation) {
        this.setTitle(movieInformation.getTitle());
        String rating = "Rating: " + movieInformation.getRating();
        mRating.setText(rating);
        String released = "Released: " + movieInformation.getReleaseDate();
        mReleaseDate.setText(released);
        mPlot.setText(movieInformation.getOverview());
        Picasso.get().load("http://image.tmdb.org/t/p/w500" + movieInformation.getImage()).into(mMoviePoster);
        Picasso.get().load("http://image.tmdb.org/t/p/w185" + movieInformation.getImage()).into(mFullPoster);
    }


}
