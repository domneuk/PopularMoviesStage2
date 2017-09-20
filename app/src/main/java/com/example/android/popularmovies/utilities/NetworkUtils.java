package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final int REQUEST_CONNECT_TIMEOUT = 5000;
    private static final int REQUEST_READ_TIMEOUT = 10000;

    private final static String API_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private final static String API_ENDPOINT_TOP_RATED = "top_rated";
    private final static String API_ENDPOINT_POPULAR = "popular";
    private final static String API_ENDPOINT_TRAILERS = "videos";
    private final static String API_ENDPOINT_REVIEWS = "reviews";

    private final static String API_KEY_PARAM = "api_key";
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    private final static String API_LANGUAGE_PARAM = "language";
    private final static String API_LANGUAGE = "en-US";

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private final static String DEFAULT_IMAGE_SIZE = "w185";
    private final static String SMALL_IMAGE_SIZE = "w92";
    private final static String BIG_IMAGE_SIZE = "w500";

    public static final String SORTING_TOP_RATED = "TOP_RATED";
    public static final String SORTING_POPULAR = "POPULAR";

    private static final String BASE_YOUTUBE_VIDEO_URL = "https://www.youtube.com";
    private static final String BASE_YOUTUBE_IMAGE_URL = "https://img.youtube.com";

    private static final String YOUTUBE_VIDEO_PATH = "watch";
    private static final String YOUTUBE_VIDEO_QUERY_PARAM = "v";
    private static final String YOUTUBE_IMAGE_PATH = "vi";
    private static final String YOUTUBE_IMAGE_ENDING = "0.jpg";

    public static URL buildUrl(String sorting) {
        String endpoint;
        if (sorting.toUpperCase().equals(SORTING_TOP_RATED)) {
            endpoint = API_ENDPOINT_TOP_RATED;
        } else {
            endpoint = API_ENDPOINT_POPULAR;
        }
        Uri builtUri = Uri.parse(API_BASE_URL + endpoint).buildUpon()
                .appendQueryParameter(API_LANGUAGE_PARAM, API_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static Uri buildUri(String sorting) {
        String endpoint;
        if (sorting.toUpperCase().equals(SORTING_TOP_RATED)) {
            endpoint = API_ENDPOINT_TOP_RATED;
        } else {
            endpoint = API_ENDPOINT_POPULAR;
        }
        return Uri.parse(API_BASE_URL + endpoint).buildUpon()
                .appendQueryParameter(API_LANGUAGE_PARAM, API_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

    public static URL buildTrailerUrl(String movieId) {
        Uri builtUri = buildTrailerUri(movieId);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildTrailerUri(String movieId) {
        return Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath(API_ENDPOINT_TRAILERS)
                .appendQueryParameter(API_LANGUAGE_PARAM, API_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

    public static URL buildReviewUrl(String movieId) {
        Uri builtUri = buildReviewUri(movieId);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildReviewUri(String movieId) {
        return Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath(API_ENDPOINT_REVIEWS)
                .appendQueryParameter(API_LANGUAGE_PARAM, API_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

    public static String buildYoutubeVideoUrl(String videoKey) {
        return Uri.parse(BASE_YOUTUBE_VIDEO_URL).buildUpon()
                .appendPath(YOUTUBE_VIDEO_PATH)
                .appendQueryParameter(YOUTUBE_VIDEO_QUERY_PARAM, videoKey)
                .build().toString();
    }

    public static String buildYoutubeImageUrl(String videoKey) {
        return Uri.parse(BASE_YOUTUBE_IMAGE_URL).buildUpon()
                .appendEncodedPath(YOUTUBE_IMAGE_PATH)
                .appendEncodedPath(videoKey)
                .appendEncodedPath(YOUTUBE_IMAGE_ENDING)
                .build().toString();
    }

    public static Uri buildPosterUri(String imageUri) {
        return Uri.parse(BASE_IMAGE_URL + DEFAULT_IMAGE_SIZE + imageUri).buildUpon()
                .build();
    }

    public static Uri buildPosterUri(String imageUri, String size) {
        String uri = BASE_IMAGE_URL;
        switch (size.toUpperCase()) {
            case "SMALL":
                uri += SMALL_IMAGE_SIZE;
                break;
            case "BIG":
                uri += BIG_IMAGE_SIZE;
                break;
            default:
                uri += DEFAULT_IMAGE_SIZE;
        }
        uri += imageUri;
        return Uri.parse(uri).buildUpon()
                .build();
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(REQUEST_CONNECT_TIMEOUT);
        urlConnection.setReadTimeout(REQUEST_READ_TIMEOUT);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
