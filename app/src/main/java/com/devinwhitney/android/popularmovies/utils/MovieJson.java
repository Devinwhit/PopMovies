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
                allMovieInformation.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allMovieInformation;
    }
}
