package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private static final int NUM_IMBD_RATING_STARS = 10;

    private static final int MOVIE_DETAIL_TRAILER_LOADER_ID = 10;
    private static final int MOVIE_DETAIL_REVIEW_LOADER_ID = 11;

    private Movie selectedMovie;
    private String movieId;
    private Boolean isFavorite;

    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        ImageView imageView = findViewById(R.id.iv_movie_poster);
        TextView titleView = findViewById(R.id.tv_title);
        TextView originalTitleView = findViewById(R.id.tv_original_title);
        TextView originalLanguageView = findViewById(R.id.tv_original_language);
        RatingBar userRatingBar = findViewById(R.id.rb_user_rating);
        userRatingBar.setNumStars(NUM_IMBD_RATING_STARS);
        TextView releaseDateView = findViewById(R.id.tv_release_date);
        TextView plotSynopsisView = findViewById(R.id.tv_plot_synopsis);

        trailerRecyclerView = findViewById(R.id.rv_trailer);
        reviewRecyclerView = findViewById(R.id.rv_review);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite();
            }
        });

        Intent receivedIntent = getIntent();

        if (receivedIntent != null) {
            if (receivedIntent.hasExtra(MainActivity.INTENT_MOVIE)) {
                selectedMovie = receivedIntent.getParcelableExtra(MainActivity.INTENT_MOVIE);
                movieId = String.valueOf(selectedMovie.getId());
                isFavorite = isMovieInDb(movieId);

                if (isFavorite) {
                    floatingActionButton.setImageResource(R.drawable.ic_favorite_selected);
                }

                collapsingToolbarLayout.setTitle(selectedMovie.getTitle());

                try {
                    String posterUri = selectedMovie.getPosterUri();
                    Uri imageUrl = NetworkUtils.buildPosterUri(posterUri, "big");
                    Picasso.with(this).load(imageUrl).into(imageView);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                titleView.setText(selectedMovie.getTitle());
                originalTitleView.setText(selectedMovie.getOriginalTitle());
                originalLanguageView.setText(selectedMovie.getOriginalLanguage().toUpperCase());
                userRatingBar.setRating(selectedMovie.getUserRating().floatValue());
                Date releaseDate = selectedMovie.getReleaseDate();
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(releaseDate);
                releaseDateView.setText(formattedDate);
                plotSynopsisView.setText(selectedMovie.getPlotSynopsis());

                getSupportLoaderManager().initLoader(MOVIE_DETAIL_TRAILER_LOADER_ID, null, this);
                TrailerAdapter trailerAdapter = new TrailerAdapter(this, null);
                trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                trailerRecyclerView.setAdapter(trailerAdapter);

                getSupportLoaderManager().initLoader(MOVIE_DETAIL_REVIEW_LOADER_ID, null, this);
                ReviewAdapter reviewAdapter = new ReviewAdapter(this, null);
                reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                reviewRecyclerView.setAdapter(reviewAdapter);
            } else
                Log.d(TAG, "No movie was set in the intent.");
        } else
            Log.d(TAG, "Activity called without an intent.");
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == MOVIE_DETAIL_TRAILER_LOADER_ID) {
            return new TrailerLoader(this, movieId);
        } else if (id == MOVIE_DETAIL_REVIEW_LOADER_ID) {
            return new ReviewLoader(this, movieId);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();

        if (id == MOVIE_DETAIL_TRAILER_LOADER_ID) {
            TrailerAdapter trailerAdapter = new TrailerAdapter(this, (List<Trailer>) data);
            trailerRecyclerView.setAdapter(trailerAdapter);
        } else if (id == MOVIE_DETAIL_REVIEW_LOADER_ID) {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, (List<Review>) data);
            reviewRecyclerView.setAdapter(reviewAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();

        if (id == MOVIE_DETAIL_TRAILER_LOADER_ID) {
            trailerRecyclerView.setAdapter(null);
        } else if (id == MOVIE_DETAIL_REVIEW_LOADER_ID) {
            reviewRecyclerView.setAdapter(null);
        }
    }

    private boolean isMovieInDb(String movieId) {
        Uri queryUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(movieId).build();
        String[] projection = new String[]{movieId};
        Cursor cursor = getContentResolver().query(queryUri, projection, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        return false;
    }

    private void deleteMovieFromDb() {
        getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(movieId).build(), null, null);
    }

    private void insertMovieIntoDb() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, selectedMovie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, selectedMovie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, selectedMovie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE, selectedMovie.getOriginalLanguage());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING, selectedMovie.getUserRating());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, selectedMovie.getReleaseDate().toString());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, selectedMovie.getPlotSynopsis());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URI, selectedMovie.getPosterUri());
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    public void toggleFavorite() {
        if (isFavorite) {
            deleteMovieFromDb();
            isFavorite = false;
            floatingActionButton.setImageResource(R.drawable.ic_favorite_unselected);
        } else {
            isFavorite = true;
            floatingActionButton.setImageResource(R.drawable.ic_favorite_selected);
            insertMovieIntoDb();
        }
    }
}