package com.example.android.popularmovies;

import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Trailer {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    private String videoUrl;
    private String imageUrl;

    public Trailer(String id, String key, String name, String site, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;

        this.videoUrl = NetworkUtils.buildYoutubeVideoUrl(this.key);
        this.imageUrl = NetworkUtils.buildYoutubeImageUrl(this.key);
    }

    public Trailer(String video, String image) {
        this.videoUrl = video;
        this.imageUrl = image;
    }

    private Trailer(JSONObject trailer){
        try {
            this.id = trailer.getString("id");
            this.key = trailer.getString("key");
            this.name = trailer.getString("name");
            this.site = trailer.getString("site");
            this.type = trailer.getString("type");

            this.videoUrl = NetworkUtils.buildYoutubeVideoUrl(this.key);
            this.imageUrl = NetworkUtils.buildYoutubeImageUrl(this.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static List<Trailer> fromJson(JSONArray trailerList) {
        List<Trailer> trailers = new ArrayList<>();
        for (int i = 0; i < trailerList.length(); i++) {
            try {
                trailers.add(new Trailer(trailerList.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trailers;
    }
}
