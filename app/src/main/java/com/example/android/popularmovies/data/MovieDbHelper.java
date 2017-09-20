package com.example.android.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_MOVIES_TABLE =
            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "("
                    + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING + " TEXT NUT NULL DEFAULT 0, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URI + " TEXT NOT NULL);";

    private static final String ALTER_MOVIES_ADD_ORIGINAL_TITLE =
            "ALTER TABLE " + MovieContract.MovieEntry.TABLE_NAME
                    + " ADD COLUMN " + MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL;";
    private static final String ALTER_MOVIES_ADD_ORIGINAL_LANGUAGE =
            "ALTER TABLE " + MovieContract.MovieEntry.TABLE_NAME
                    + " ADD COLUMN " + MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE + " TEXT NOT NULL;";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(ALTER_MOVIES_ADD_ORIGINAL_TITLE);
            db.execSQL(ALTER_MOVIES_ADD_ORIGINAL_LANGUAGE);
        }
    }

}
