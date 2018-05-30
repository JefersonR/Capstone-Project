package br.wake_in_place.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import br.wake_in_place.R;


/**
 * Created by Jeferson on 17/03/2018.
 */

public class PlacesContentProvider extends ContentProvider {
    private WakePlaceDB movieDB;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        movieDB = new WakePlaceDB(ctx);
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = WakePlaceDBContract.URI_MATCHER.match(uri);
        switch (match) {
            case WakePlaceDBContract.Movie.PATH_TOKEN:
                return WakePlaceDBContract.Movie.CONTENT_TYPE_DIR;
            case WakePlaceDBContract.Movie.PATH_FOR_ID_TOKEN:
                return WakePlaceDBContract.Movie.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = movieDB.getWritableDatabase();
        int token = WakePlaceDBContract.URI_MATCHER.match(uri);
        switch (token) {
            case WakePlaceDBContract.Movie.PATH_TOKEN: {
                long id = db.insert(WakePlaceDBContract.Movie.NAME, null, values);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                return WakePlaceDBContract.Movie.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = movieDB.getReadableDatabase();
        final int match = WakePlaceDBContract.URI_MATCHER.match(uri);
        switch (match) {
            // retrieve movie list
            case WakePlaceDBContract.Movie.PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(WakePlaceDBContract.Movie.NAME);
                return builder.query(db, null, null, null, null, null, null);
            }
            default:
                return null;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = movieDB.getWritableDatabase();
        final int match = WakePlaceDBContract.URI_MATCHER.match(uri);
        int deleteCount = 0;
        switch (match) {
            case WakePlaceDBContract.Movie.PATH_TOKEN: {
                deleteCount = db.delete(WakePlaceDBContract.Movie.NAME, WakePlaceDBContract.Movie.Cols.MOVIE_ID + "="
                        + selection, null);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return deleteCount;
    }


    public boolean contains(String movieID, Context context) {
        movieDB = new WakePlaceDB(context);
        SQLiteDatabase db = movieDB.getWritableDatabase();
        String Query = String.format(context.getString(R.string.str_query_contains), WakePlaceDBContract.Movie.NAME, WakePlaceDBContract.Movie.Cols.MOVIE_ID, movieID);
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}