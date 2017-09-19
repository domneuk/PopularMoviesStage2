package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.utilities.MovieApiUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {


    private static final String TAG = TrailerLoader.class.getSimpleName();

    private List<Trailer> trailers;
    private String movieId;

    public TrailerLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (trailers == null) {
            forceLoad();
        }
    }

    @Override
    public List<Trailer> loadInBackground() {

        URL apiUrl = NetworkUtils.buildTrailerUrl(movieId);

        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            return MovieApiUtils.getListOfTrailersFromJson(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(List<Trailer> data) {
        trailers = data;
        super.deliverResult(data);
    }
}
