package com.devinwhitney.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by devin on 6/5/2018.
 */

public class MovieContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.devinwhitney.android.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIES = "favoriteMovies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favoriteMovies";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_IMAGE = "image";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RATING= "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_REVIEWS = "reviews";
        public static final String COLUMN_MOVIE_TRAILERS = "trailers";

    }
}
