package com.devinwhitney.android.popularmovies.utils;


import com.devinwhitney.android.popularmovies.model.MovieInformation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by devin on 5/8/2018.
 */

public class MovieUtils {

    public static ArrayList<MovieInformation> getMostPopular(int pageNum, String apiKey) throws IOException {
        URL url = null;
        try {
            url = new URL("https://api.themoviedb.org/3/movie/popular?page=" + pageNum + "&language=en-US&api_key=" + apiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder buff = new StringBuilder();
        assert url != null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            boolean hasMore = scan.hasNext();
            if (hasMore) {
                buff.append(scan.next());
            }
        } finally {
            urlConnection.disconnect();
        }
        return MovieJson.getMovieInfoFromJson(buff.toString());
    }

    public static ArrayList<MovieInformation> getHighestRated(int pageNum, String apiKey) throws IOException {
        System.out.println("get highest rated called!");
        URL url = null;
        try {
            url = new URL("https://api.themoviedb.org/3/movie/top_rated?page=" + pageNum + "&language=en-US&api_key=" + apiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder buff = new StringBuilder();
        assert url != null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            boolean hasMore = scan.hasNext();
            if (hasMore) {
                buff.append(scan.next());
            }
        }
        finally {
            urlConnection.disconnect();
        }
        return MovieJson.getMovieInfoFromJson(buff.toString());
    }

    public static ArrayList<String> getReviews(int movieId, String apiKey) throws IOException {
        URL url = null;
        try {
            url = new URL("https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + apiKey + "&language=en-US&page=1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder buff = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            boolean hasMore = scan.hasNext();
            if (hasMore) {
                buff.append(scan.next());
            }
        } finally {
            urlConnection.disconnect();
        }
        return MovieJson.getReviews(buff.toString());
    }

    public static ArrayList<String> getTrailers(int movieId, String apiKey) throws IOException {
        URL url = null;
        try {
            url = new URL("https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + apiKey + "&language=en-US&page=1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder buff = new StringBuilder();
        HttpURLConnection  urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            boolean hasMore = scan.hasNext();
            if (hasMore) {
                buff.append(scan.next());
            }
        }
        finally {
            urlConnection.disconnect();
        }
        return MovieJson.getTrailers(buff.toString());
    }

}
