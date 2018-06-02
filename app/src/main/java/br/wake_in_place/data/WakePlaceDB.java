package br.wake_in_place.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeferson on 17/03/2018.
 */

public class WakePlaceDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wake_place.db";
    private static final int DATABASE_VERSION = 2;

    public WakePlaceDB(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                WakePlaceDBContract.AlarmsBD.NAME+ " ( " +
                WakePlaceDBContract.AlarmsBD.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WakePlaceDBContract.AlarmsBD.Cols.DATE + " TEXT NOT NULL, " +
                WakePlaceDBContract.AlarmsBD.Cols.HOUR + " TEXT NOT NULL, " +
                WakePlaceDBContract.AlarmsBD.Cols.INTERVAL 	+ " INTEGER , " +
                WakePlaceDBContract.AlarmsBD.Cols.REPEAT_DAYS + " TEXT, " +
                WakePlaceDBContract.AlarmsBD.Cols.PLACE_ID + " TEXT, " +
                WakePlaceDBContract.AlarmsBD.Cols.ADDRESS + " TEXT, " +
                WakePlaceDBContract.AlarmsBD.Cols.RADIUS + " INTEGER, " +
                "UNIQUE (" + WakePlaceDBContract.AlarmsBD.Cols.ID + ") ON CONFLICT REPLACE)"
        );


        db.execSQL("CREATE TABLE " +
                WakePlaceDBContract.PlacesBD.NAME+ " ( " +
                WakePlaceDBContract.PlacesBD.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WakePlaceDBContract.PlacesBD.Cols.ADDRESS  + " TEXT," +
                WakePlaceDBContract.PlacesBD.Cols.NAME  + " TEXT," +
                WakePlaceDBContract.PlacesBD.Cols.PLACE_ID  + " TEXT UNIQUE," +
                WakePlaceDBContract.PlacesBD.Cols.LATITUDE + " DOUBLE, " +
                WakePlaceDBContract.PlacesBD.Cols.LONGITUDE + " DOUBLE, " +
                "UNIQUE (" + WakePlaceDBContract.PlacesBD.Cols.ID + ") ON CONFLICT REPLACE)"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + WakePlaceDBContract.AlarmsBD.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + WakePlaceDBContract.PlacesBD.NAME);
            onCreate(db);
        }
    }

}
