package br.wake_in_place.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Jeferson on 30/05/2018.
 */

public class WakePlaceContentProvider extends ContentProvider {
    private WakePlaceDB wakePlaceDB;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        wakePlaceDB = new WakePlaceDB(ctx);
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = WakePlaceDBContract.URI_MATCHER.match(uri);
        switch (match) {
            case WakePlaceDBContract.AlarmsBD.PATH_TOKEN:
                return WakePlaceDBContract.AlarmsBD.CONTENT_TYPE_DIR;
            case WakePlaceDBContract.AlarmsBD.PATH_FOR_ID_TOKEN:
                return WakePlaceDBContract.AlarmsBD.CONTENT_ITEM_TYPE;
            case WakePlaceDBContract.PlacesBD.PATH_TOKEN:
                return WakePlaceDBContract.PlacesBD.CONTENT_TYPE_DIR;
            case WakePlaceDBContract.PlacesBD.PATH_FOR_ID_TOKEN:
                return WakePlaceDBContract.PlacesBD.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = wakePlaceDB.getWritableDatabase();
        int token = WakePlaceDBContract.URI_MATCHER.match(uri);
        switch (token) {
            case WakePlaceDBContract.AlarmsBD.PATH_TOKEN: {
                long id = db.insert(WakePlaceDBContract.AlarmsBD.NAME, null, values);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                return WakePlaceDBContract.AlarmsBD.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            case WakePlaceDBContract.PlacesBD.PATH_TOKEN: {
                long id = db.insert(WakePlaceDBContract.PlacesBD.NAME, null, values);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                return WakePlaceDBContract.PlacesBD.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = wakePlaceDB.getReadableDatabase();
        final int match = WakePlaceDBContract.URI_MATCHER.match(uri);
        switch (match) {
            // retrieve movie list
            case WakePlaceDBContract.AlarmsBD.PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(WakePlaceDBContract.AlarmsBD.NAME);
                return builder.query(db, null, null, null, null, null, null);
            }
            case WakePlaceDBContract.PlacesBD.PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(WakePlaceDBContract.PlacesBD.NAME);
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
        SQLiteDatabase db = wakePlaceDB.getWritableDatabase();
        final int match = WakePlaceDBContract.URI_MATCHER.match(uri);
        int deleteCount = 0;
        switch (match) {
            case WakePlaceDBContract.AlarmsBD.PATH_TOKEN: {
                deleteCount = db.delete(WakePlaceDBContract.AlarmsBD.NAME, WakePlaceDBContract.AlarmsBD.Cols.ID + "="
                        + selection, null);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
            case WakePlaceDBContract.PlacesBD.PATH_TOKEN: {
                deleteCount = db.delete(WakePlaceDBContract.PlacesBD.NAME, WakePlaceDBContract.PlacesBD.Cols.ID + "="
                        + selection, null);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
        }
        return deleteCount;
    }


   /* public boolean contains(String movieID, Context context) {
        wakePlaceDB = new WakePlaceDB(context);
        SQLiteDatabase db = wakePlaceDB.getWritableDatabase();
        String Query = String.format(context.getString(R.string.str_query_contains), WakePlaceDBContract.Movie.NAME, WakePlaceDBContract.Movie.Cols.MOVIE_ID, movieID);
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }*/
}