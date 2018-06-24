package com.devinwhitney.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devinwhitney.android.popularmovies.data.MovieContract;
import com.devinwhitney.android.popularmovies.model.MovieInformation;
import com.devinwhitney.android.popularmovies.utils.MovieUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.devinwhitney.android.popularmovies.MovieAdapter.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieInformation>>, MovieAdapterOnClickHandler, MovieAdapterScrollListener {

    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private ImageView mMoviePoster;
    private int pageNum = 1;
    private String sort = "popular";
    private Parcelable mLayoutState;
    private GridLayoutManager gridLayoutManager;

    private boolean newSort = false;

    private static final String STATE_KEY = "LAYOUT";
    private static final String SORT_KEY = "sort";
    private static final String KEEP_PAGENUM = "pageNum";
    private static final String MOVIE_ARRAY = "movieArray";
    private static final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        recyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMoviePoster = findViewById(R.id.movie_image);
        LoaderManager.LoaderCallbacks<ArrayList<MovieInformation>> callback = MainActivity.this;

        if (savedInstanceState != null){
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, savedInstanceState, callback);
        } else {
            Bundle bundleForLoader = new Bundle();
            bundleForLoader.putString("sort", "popular");

            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundleForLoader, callback);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLayoutState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_KEY, mLayoutState);
        outState.putString(SORT_KEY, sort);
        outState.putInt(KEEP_PAGENUM, pageNum);
        ArrayList<MovieInformation> info = mMovieAdapter.getMovieInformation();
        outState.putParcelableArrayList(MOVIE_ARRAY, info);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(STATE_KEY);
            sort = savedInstanceState.getString(SORT_KEY);
            pageNum = savedInstanceState.getInt(KEEP_PAGENUM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLayoutState != null) {
            gridLayoutManager.onRestoreInstanceState(mLayoutState);
        }
    }

    @Override
    public Loader<ArrayList<MovieInformation>> onCreateLoader(int id, final Bundle args) {
        return new  AsyncTaskLoader<ArrayList<MovieInformation>>(this) {

            ArrayList<MovieInformation> mMovieInformation = args.getParcelableArrayList(MOVIE_ARRAY);
            String apiKey = getString(R.string.API);
            @Override
            protected void onStartLoading() {
                if (mMovieInformation != null) {

                    deliverResult(mMovieInformation);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieInformation> loadInBackground() {
                ArrayList<MovieInformation> info = new ArrayList<>();
                String sortTask = args.getString("sort");
                int taskPageNum = 1;
                if (sortTask == null) {
                    sortTask = sort;
                }
                if (sortTask.equals("favorites")) {
                    info = getFavorites();
                }else {
                    if (args.containsKey("pageNum")) {
                        taskPageNum = args.getInt("pageNum");
                    }

                    try {
                        if (sortTask.equals("popular")) {
                            info = MovieUtils.getMostPopular(taskPageNum, apiKey);
                        } else if (sortTask.equals("rated")) {
                            info = MovieUtils.getHighestRated(taskPageNum, apiKey);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (MovieInformation movie : info) {
                        int movieId = movie.getMovieId();
                        System.out.println("movie id : " + movieId);
                        try {
                            ArrayList<String> reviews = MovieUtils.getReviews(movieId, apiKey);
                            try {
                                ArrayList<String> trailers = MovieUtils.getTrailers(movieId, apiKey);
                                movie.setTrailers(trailers);
                            } catch (Exception e) {
                                movie.setTrailers(new ArrayList<String>());
                            }
                            movie.setReviews(reviews);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                return info;
            }

            @Override
            public void deliverResult(ArrayList<MovieInformation> data) {
                mMovieInformation = data;
                super.deliverResult(data);

            }

        };
    }

    private ArrayList<MovieInformation> getFavorites() {
        ArrayList<MovieInformation> movies = new ArrayList<>();

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        Cursor cursor  = getContentResolver().query(uri,null,null,null,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MovieInformation movieInformation = new MovieInformation();
                movieInformation.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                movieInformation.setImage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE)));
                movieInformation.setMovieId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movieInformation.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                movieInformation.setRating(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING)));
                movieInformation.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
                ArrayList<String> allReviews = new ArrayList<>();
                String[] review = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_REVIEWS)).split("\\r?\\n");
                Collections.addAll(allReviews, review);
                movieInformation.setReviews(allReviews);
                ArrayList<String> allTrailers = new ArrayList<>();
                List<String> list = Arrays.asList(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILERS)).split(","));
                allTrailers.addAll(list);
                movieInformation.setTrailers(allTrailers);
                movies.add(movieInformation);
                cursor.moveToNext();
            }
        }
        return movies;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieInformation>> loader, ArrayList<MovieInformation> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (mMovieAdapter.getMovieInformation().equals(data) && data != null) {
            System.out.println("SAME ARRAY, DO NOT SET SAME DATA!!!!");
        } else {
            mMovieAdapter.setMovieInformation(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieInformation>> loader) {

    }


    /**
     * This method is called when the user wishes to resort the data. We scrub the movie title ArrayList,
     * in addition to resetting the pageNum for the movie DB to be loaded since we want to start at the
     * beginning again
     */
    private void invalidateData() {
        mMovieAdapter.clearMovieInformation();
        pageNum = 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.sort, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args = new Bundle();
        if (id == R.id.sort_popular) {
            if (sort.equals("popular")) {
                Toast.makeText(this, "Most Popular already selected!", Toast.LENGTH_SHORT).show();
            } else{
                invalidateData();
                args.putParcelableArrayList(MOVIE_ARRAY, null);
                args.putString("sort", "popular");
                sort = "popular";
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
            }

        } else if (id == R.id.sort_top_rated) {
            if(sort.equals("rated")) {
                Toast.makeText(this, "Highest Rated already selected!", Toast.LENGTH_SHORT).show();
            } else {
                invalidateData();
                args.putParcelableArrayList(MOVIE_ARRAY, null);
                args.putString("sort", "rated");
                sort = "rated";
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
            }

        } else {
            if(sort.equals("favorites")) {
                Toast.makeText(this, "Favorites already selected!", Toast.LENGTH_SHORT).show();
            } else {
                invalidateData();
                args.putString("sort", "favorites");
                sort = "favorites";
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieInformation movieInformation) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("data", movieInformation);
        startActivity(intentToStartDetailActivity);
    }


    /**
     * This method helps the adapter by providing the current item being loaded, as well as the
     * total number of movie titles loaded. When the the current item being loaded passes the
     * total number of movies loaded, we restart the loader WITHOUT invalidating data and
     * passing the next page num for the movie db, adding 20 more movies to the ArrayList
     * @param scrollItem
     * @param movieArraySize
     */
    @Override
    public void loadMore(int scrollItem, int movieArraySize) {
        System.out.println(scrollItem + "    " + movieArraySize);
        if (scrollItem >=movieArraySize - 1 && !sort.equals("favorites")){
            Bundle args = new Bundle();
            args.putInt("pageNum", ++pageNum);
            args.putString("sort", sort);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
        }
    }


}
