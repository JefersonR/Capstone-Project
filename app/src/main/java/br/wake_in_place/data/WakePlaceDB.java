package br.wake_in_place.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeferson on 17/03/2018.
 */

public class WakePlaceDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fav_movie.db";
    private static final int DATABASE_VERSION = 2;

    public WakePlaceDB(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                WakePlaceDBContract.Movie.NAME+ " ( " +
                WakePlaceDBContract.Movie.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WakePlaceDBContract.Movie.Cols.MOVIE_ID  + " TEXT UNIQUE," +
                WakePlaceDBContract.Movie.Cols.TITLE + " TEXT NOT NULL, " +
                WakePlaceDBContract.Movie.Cols.ORIGINAL_TITLE 	+ " TEXT , " +
                WakePlaceDBContract.Movie.Cols.RELEASE_DATE + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.LANGUAGE + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.POSTER + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.PLOT + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.VOTE_AVERAGE + " DOUBLE, " +
                "UNIQUE (" + WakePlaceDBContract.Movie.Cols.ID + ") ON CONFLICT REPLACE)"
        );
      /*  db.execSQL("CREATE TABLE " +
                WakePlaceDBContract.Movie.NAME+ " ( " +
                WakePlaceDBContract.Movie.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WakePlaceDBContract.Movie.Cols.MOVIE_ID  + " TEXT UNIQUE," +
                WakePlaceDBContract.Movie.Cols.TITLE + " TEXT NOT NULL, " +
                WakePlaceDBContract.Movie.Cols.ORIGINAL_TITLE 	+ " TEXT , " +
                WakePlaceDBContract.Movie.Cols.RELEASE_DATE + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.LANGUAGE + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.POSTER + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.PLOT + " TEXT, " +
                WakePlaceDBContract.Movie.Cols.VOTE_AVERAGE + " DOUBLE, " +
                "UNIQUE (" + WakePlaceDBContract.Movie.Cols.ID + ") ON CONFLICT REPLACE)"
        );*/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + WakePlaceDBContract.Movie.NAME);
            onCreate(db);
        }
    }

}
