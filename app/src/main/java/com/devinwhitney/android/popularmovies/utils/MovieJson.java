package com.devinwhitney.android.popularmovies.utils;

import com.devinwhitney.android.popularmovies.model.MovieInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by devin on 5/8/2018.
 */

public class MovieJson {

    public static ArrayList<MovieInformation> getMovieInfoFromJson(String json) {
        ArrayList<MovieInformation> allMovieInformation = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int x = 0; x < 20; x++) {
                JSONObject obj = jsonArray.getJSONObject(x);
                MovieInformation info = new MovieInformation();
                info.setTitle(obj.getString("title"));
                info.setOverview(obj.getString("overview"));
                info.setRating(obj.getString("vote_average"));
                info.setReleaseDate(obj.getString("release_date"));
                info.setImage(obj.getString("poster_path"));
                info.setMovieId(Integer.parseInt(obj.getString("id")));
                allMovieInformation.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allMovieInformation;
    }

    public static ArrayList<String> getReviews(String json) {
        ArrayList<String[]> reviews = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject obj = jsonArray.getJSONObject(x);
                String author = obj.getString("author");
                String content = obj.getString("content");
                test.add(content);
                String toAdd[] = {author, content};
                reviews.add(toAdd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return test;
    }

    public static ArrayList<String> getTrailers(String json) {
        ArrayList<String> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject obj = jsonArray.getJSONObject(x);
                String key = obj.getString("key");
                String video = obj.getString("type");
                if (video.equals("Trailer")) {
                    trailers.add(key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;

    }
}
