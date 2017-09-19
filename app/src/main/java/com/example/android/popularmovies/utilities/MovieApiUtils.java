package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.Movie;
import com.example.android.popularmovies.Review;
import com.example.android.popularmovies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

public final class MovieApiUtils {

    public static List<Movie> getListOfMoviesFromJson(String apiJsonStr) throws JSONException {

        final String LIST_OBJECT = "results";
        final String ERROR_MSG = "status_message";
        final String ERROR_CODE = "status_code";

        List<Movie> moviesList;

        JSONObject apiJson = new JSONObject(apiJsonStr);

        if (apiJson.has(ERROR_MSG) || apiJson.has(ERROR_CODE)) {
            int errorCode = apiJson.getInt(ERROR_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray moviesArray = apiJson.getJSONArray(LIST_OBJECT);
        moviesList = Movie.fromJson(moviesArray);

        return moviesList;
    }

    public static List<Trailer> getListOfTrailersFromJson(String apiJsonStr) throws JSONException {

        final String LIST_OBJECT = "results";
        final String ERROR_MSG = "status_message";
        final String ERROR_CODE = "status_code";

        List<Trailer> trailersList;

        JSONObject apiJson = new JSONObject(apiJsonStr);

        if (apiJson.has(ERROR_MSG) || apiJson.has(ERROR_CODE)) {
            int errorCode = apiJson.getInt(ERROR_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray trailerArray = apiJson.getJSONArray(LIST_OBJECT);
        trailersList = Trailer.fromJson(trailerArray);

        return trailersList;
    }

    public static List<Review> getListOfReviewsFromJson(String apiJsonStr) throws JSONException {

        final String LIST_OBJECT = "results";
        final String ERROR_MSG = "status_message";
        final String ERROR_CODE = "status_code";

        List<Review> reviewsList;

        JSONObject apiJson = new JSONObject(apiJsonStr);

        if (apiJson.has(ERROR_MSG) || apiJson.has(ERROR_CODE)) {
            int errorCode = apiJson.getInt(ERROR_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray reviewArray = apiJson.getJSONArray(LIST_OBJECT);
        reviewsList = Review.fromJson(reviewArray);

        return reviewsList;
    }

}
