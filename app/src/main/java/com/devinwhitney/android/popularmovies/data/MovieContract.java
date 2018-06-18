package com.devinwhitney.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by devin on 6/5/2018.
 */

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {
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
