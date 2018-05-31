package br.wake_in_place.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jeferson on 17/03/2018.
 */

public class WakePlaceDBContract {
    public static final String AUTHORITY = "udacity.contentprovider.wake";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    private WakePlaceDBContract() {
    };

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;

        matcher.addURI(authority, AlarmsBD.PATH, AlarmsBD.PATH_TOKEN);
        matcher.addURI(authority, AlarmsBD.PATH_FOR_ID, AlarmsBD.PATH_FOR_ID_TOKEN);
        matcher.addURI(authority, PlacesBD.PATH, PlacesBD.PATH_TOKEN);
        matcher.addURI(authority, PlacesBD.PATH_FOR_ID, PlacesBD.PATH_FOR_ID_TOKEN);

        return matcher;
    }

    public static class AlarmsBD {
        public static final String NAME = "alarms_bd";

        public static final String PATH = "alarms_bd";
        public static final int PATH_TOKEN = 100;
        public static final String PATH_FOR_ID = "alarms_bd/*";
        public static final int PATH_FOR_ID_TOKEN = 200;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.alarms_bd.app";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.alarms_bd.app";

        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            public static final String DATE = "date";
            public static final String HOUR = "hour";
            public static final String INTERVAL = "interval";
            public static final String REPEAT_DAYS = "repeat_days";
            public static final String PLACE_ID = "place_id";
            public static final String RADIUS = "radius";
        }

    }

    public static class PlacesBD {
        public static final String NAME = "places_bd";

        public static final String PATH = "places_bd";
        public static final int PATH_TOKEN = 101;
        public static final String PATH_FOR_ID = "places_bd/*";
        public static final int PATH_FOR_ID_TOKEN = 201;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.places_bd.app";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.places_bd.app";

        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            public static final String ADDRESS_ID = "address_id";
            public static final String ADDRESS = "address";
            public static final String ADDRESS_IMG = "address_img";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
        }

    }
}