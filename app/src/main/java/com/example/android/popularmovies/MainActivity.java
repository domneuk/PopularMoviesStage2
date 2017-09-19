package com.example.android.popularmovies;

import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMovieOverview;
    private ProgressBar mLoadingIndicator;

    private MenuItem mActionSortPopular;
    private MenuItem mActionSortTopRated;
    private MenuItem mActionFavorites;

    private static final String BUNDLE_ID = "loaderId";

    private int loaderId;

    public static final int MOVIE_POPULAR_LOADER_ID = 1;
    public static final int MOVIE_TOPRATED_LOADER_ID = 2;
    public static final int MOVIE_FAVORITE_LOADER_ID = 3;

    public static final String INTENT_MOVIE = "movie";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_ID, loaderId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_ID)) {
            loaderId = savedInstanceState.getInt(BUNDLE_ID);
            getSupportLoaderManager().initLoader(loaderId, null, this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieOverview = findViewById(R.id.rv_movie_overview);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState == null) {
            loaderId = MOVIE_POPULAR_LOADER_ID;
            getSupportLoaderManager().initLoader(loaderId, null, this);
        }

        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 3;
        }

        mMovieOverview.setLayoutManager(new GridLayoutManager(this, spanCount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_actions, menu);

        mActionSortPopular = menu.findItem(R.id.action_sort_popular);
        mActionSortTopRated = menu.findItem(R.id.action_sort_toprated);
        mActionFavorites = menu.findItem(R.id.action_favorites);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setEnabled(false);

        String newTitle;

        switch (item.getItemId()) {
            case R.id.action_sort_toprated:
                mActionSortPopular.setEnabled(true);
                mActionFavorites.setEnabled(true);
                loaderId = MOVIE_TOPRATED_LOADER_ID;
                newTitle = getString(R.string.title_sort_by_toprated);
                getSupportLoaderManager().destroyLoader(MOVIE_FAVORITE_LOADER_ID);
                break;
            case R.id.action_sort_popular:
                mActionSortTopRated.setEnabled(true);
                mActionFavorites.setEnabled(true);
                loaderId = MOVIE_POPULAR_LOADER_ID;
                newTitle = getString(R.string.title_sort_by_popular);
                getSupportLoaderManager().destroyLoader(MOVIE_FAVORITE_LOADER_ID);
                break;
            case R.id.action_favorites:
                loaderId = MOVIE_FAVORITE_LOADER_ID;
                mActionSortPopular.setEnabled(true);
                mActionSortTopRated.setEnabled(true);
                newTitle = getString(R.string.title_show_favorites);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        getSupportActionBar().setTitle(newTitle);
        getSupportLoaderManager().initLoader(loaderId, null, this);

        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        mLoadingIndicator.setVisibility(View.VISIBLE);

        if (id == MOVIE_POPULAR_LOADER_ID || id == MOVIE_TOPRATED_LOADER_ID) {
            String sorting = id == MOVIE_TOPRATED_LOADER_ID ? NetworkUtils.SORTING_TOP_RATED : NetworkUtils.SORTING_POPULAR;
            return new MovieLoader(this, sorting);
        } else if (id == MOVIE_FAVORITE_LOADER_ID) {
            String[] projection = new String[]{
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING,
                    MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS,
                    MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URI
            };
            return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, projection, null, null, null);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        int loaderId = loader.getId();

        if (loaderId == MOVIE_POPULAR_LOADER_ID || loaderId == MOVIE_TOPRATED_LOADER_ID) {
            MovieAdapter movieAdapter = new MovieAdapter(this, (List<Movie>) data);
            mMovieOverview.setAdapter(movieAdapter);
        } else if (loaderId == MOVIE_FAVORITE_LOADER_ID) {
            FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this);
            mMovieOverview.setAdapter(favoriteAdapter);
            favoriteAdapter.setCursor((Cursor) data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMovieOverview.setAdapter(null);
    }

}
