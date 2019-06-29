package com.jmindel.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    private String title, overview, posterPath, backdropPath, releaseDate;
    private double rating;
    private Integer id;

    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        releaseDate = object.getString("release_date");
        rating = object.getDouble("vote_average");
        id = object.getInt("id");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public Integer getId() {
        return id;
    }
}
