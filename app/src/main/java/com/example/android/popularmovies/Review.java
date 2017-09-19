package com.example.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    private Review(JSONObject review){
        try {
            this.id = review.getString("id");
            this.author = review.getString("author");
            this.content = review.getString("content");
            this.url = review.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static List<Review> fromJson(JSONArray reviewList) {
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < reviewList.length(); i++) {
            try {
                reviews.add(new Review(reviewList.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }
}
