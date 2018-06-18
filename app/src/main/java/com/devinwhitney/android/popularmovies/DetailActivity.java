package com.devinwhitney.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinwhitney.android.popularmovies.data.MovieContract;
import com.devinwhitney.android.popularmovies.data.MovieDbHelper;
import com.devinwhitney.android.popularmovies.model.MovieInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by devin on 5/9/2018.
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private ImageView mMoviePoster;
    private ImageView mFullPoster;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mPlot;
    private TextView mReviews;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SQLiteDatabase mDb;
    private Button mButton;
    private boolean favorite;

    private ArrayList<String> mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        final MovieInformation movieInformation = intent.getParcelableExtra("data");

        mMoviePoster = findViewById(R.id.movie_poster);
        mFullPoster = findViewById(R.id.full_poster);
        mRating = findViewById(R.id.rating);
        mReleaseDate = findViewById(R.id.release_date);
        mPlot = findViewById(R.id.plot);
        mReviews = findViewById(R.id.reviews);

        mTrailers = movieInformation.getTrailers();

        mRecyclerView = findViewById(R.id.trailerRV);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TrailerAdapter(this, mTrailers);
        mRecyclerView.setAdapter(mAdapter);

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        mButton = findViewById(R.id.favButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Favorite Button pressed!");

                if (mButton.getText().equals("Favorite")) {
                    addFavorite(movieInformation.getTitle(),
                            movieInformation.getRating(),
                            movieInformation.getImage(),
                            movieInformation.getReleaseDate(),
                            String.valueOf(movieInformation.getReviews()),
                            String.valueOf(movieInformation.getTrailers()),
                            movieInformation.getOverview(),
                            movieInformation.getMovieId());
                    mButton.setText("Unfavorite");
                }
                else {
                    removeFavorite(movieInformation.getMovieId());
                    mButton.setText("Favorite");
                }
            }

        });

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
        StringBuilder addReview = new StringBuilder("");
        if (!movieInformation.getReviews().isEmpty()) {
            for (String review : movieInformation.getReviews()) {
                addReview.append(review + "\n");
            }
            mReviews.setText(addReview.toString());
        } else {
            mReviews.setText("No reviews");
        }
        favorite = checkFavorite(movieInformation.getMovieId());
        if (favorite) {
            mButton.setText("Unfavorite");
        } else {
            mButton.setText("Favorite");
        }
    }


    @Override
    public void onClick(String trailer) {
        Intent app = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer));
        Intent internet = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer));
        try {
            this.startActivity(app);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(internet);
        }

    }

    private long addFavorite(String name, String rating, String image, String release, String reviews, String trailers, String overview, int movieId) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, name);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, rating);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, image);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, release);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_REVIEWS, reviews);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILERS, trailers);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        return mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
    }

    private boolean removeFavorite(long id) {
        return mDb.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + id, null) > 0;
    }

    private boolean checkFavorite(long id) {
        Cursor query = mDb.query(MovieContract.MovieEntry.TABLE_NAME,null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + id,null,null,null,null);
        if (query.getCount() > 0) {
            return true;
        }
        return false;

    }

}
