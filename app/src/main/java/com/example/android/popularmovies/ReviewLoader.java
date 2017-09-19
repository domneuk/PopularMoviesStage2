package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.popularmovies.utilities.MovieApiUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    private static final String TAG = ReviewLoader.class.getSimpleName();

    private List<Review> reviews;
    private final String movieId;

    public ReviewLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (reviews == null) {
            forceLoad();
        }
    }

    @Override
    public List<Review> loadInBackground() {

        URL apiUrl = NetworkUtils.buildReviewUrl(movieId);
        Log.d(TAG, apiUrl.toString());

        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            return MovieApiUtils.getListOfReviewsFromJson(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(List<Review> data) {
        reviews = data;
        super.deliverResult(data);
    }
}
