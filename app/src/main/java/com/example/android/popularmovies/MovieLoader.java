package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.utilities.MovieApiUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String TAG = MovieLoader.class.getSimpleName();

    private List<Movie> movies;
    private String sorting;

    public MovieLoader(Context ctx, String sorting) {
        super(ctx);
        this.sorting = sorting;
    }

    @Override
    protected void onStartLoading() {
        if (movies == null) {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {

        if (sorting == null) {
            sorting = NetworkUtils.SORTING_POPULAR;
        }

        URL apiUrl = NetworkUtils.buildUrl(sorting);

        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            return MovieApiUtils.getListOfMoviesFromJson(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(data);
    }

}
