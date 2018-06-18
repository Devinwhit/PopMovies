package com.devinwhitney.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by devin on 5/8/2018.
 */

public class MovieInformation implements Parcelable {

    private String title;
    private String image;
    private String overview;
    private String rating;
    private String releaseDate;
    private int movieId;
    private ArrayList<String> reviews;
    private ArrayList<String> trailers;


    private MovieInformation(Parcel in) {
        title = in.readString();
        image = in.readString();
        overview = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        movieId = in.readInt();
        reviews = in.createStringArrayList();
        trailers = in.createStringArrayList();
    }

    public static final Creator<MovieInformation> CREATOR = new Creator<MovieInformation>() {
        @Override
        public MovieInformation createFromParcel(Parcel in) {
            return new MovieInformation(in);
        }

        @Override
        public MovieInformation[] newArray(int size) {
            return new MovieInformation[size];
        }
    };

    public MovieInformation() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(overview);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeInt(movieId);
        parcel.writeStringList(reviews);
        parcel.writeStringList(trailers);
    }
}
