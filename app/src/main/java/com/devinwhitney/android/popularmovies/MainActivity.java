package com.devinwhitney.android.popularmovies;

import android.content.Intent;
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

import com.devinwhitney.android.popularmovies.model.MovieInformation;
import com.devinwhitney.android.popularmovies.utils.MovieUtils;

import java.io.IOException;
import java.util.ArrayList;

import static com.devinwhitney.android.popularmovies.MovieAdapter.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieInformation>>, MovieAdapterOnClickHandler, MovieAdapterScrollListener {

    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private ImageView mMoviePoster;
    private int pageNum = 1;
    private String sort = "popular";


    private static final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        recyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMoviePoster = findViewById(R.id.movie_image);

        LoaderManager.LoaderCallbacks<ArrayList<MovieInformation>> callback = MainActivity.this;

        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("sort", "popular");
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundleForLoader, callback);


    }


    @Override
    public Loader<ArrayList<MovieInformation>> onCreateLoader(int id, final Bundle args) {
        return new  AsyncTaskLoader<ArrayList<MovieInformation>>(this) {

            ArrayList<MovieInformation> mMovieInformation = null;
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
                String sort = args.getString("sort");
                if (args.containsKey("pageNum")) {
                    pageNum = args.getInt("pageNum");
                }

                try {
                    if (sort.equals("popular"))
                    {
                        info = MovieUtils.getMostPopular(pageNum, apiKey);
                    } else {
                        info = MovieUtils.getHighestRated(pageNum, apiKey);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
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

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieInformation>> loader, ArrayList<MovieInformation> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovieInformation(data);

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
        mMovieAdapter.setMovieInformation(null);
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
                args.putString("sort", "popular");
                sort = "popular";
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
            }

        } else {
            if(sort.equals("rated")) {
                Toast.makeText(this, "Highest Rated already selected!", Toast.LENGTH_SHORT).show();
            } else {
                invalidateData();
                args.putString("sort", "rated");
                sort = "rated";
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
        if (scrollItem >=movieArraySize - 1){
            Bundle args = new Bundle();
            args.putInt("pageNum", ++pageNum);
            args.putString("sort", sort);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
        }
    }
}
