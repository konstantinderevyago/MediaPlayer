package by.itrex.jetfirer.mediaplayer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.enums.Repeat;
import by.itrex.jetfirer.mediaplayer.model.Track;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class Utils {

    public static final String RANDOM = "RANDOM";
    public static final String REPEAT = "REPEAT";

    public static String convertDuration(int value) {
        value /= 1000;
        int minutes = value / 60;
        int seconds = value - minutes * 60;

        StringBuilder durationBuilder = new StringBuilder();
        if (minutes < 10) {
            durationBuilder.append("0");
        }
        durationBuilder.append(minutes).append(":");
        if (seconds < 10) {
            durationBuilder.append("0");
        }
        durationBuilder.append(seconds);
        return durationBuilder.toString();
    }

    public static List<Track> getAllTracks(Context context) {
        List<Track> tracks = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                long id = cursor.getLong(idColumn);
                String artist = cursor.getString(artistColumn);
                String title = cursor.getString(titleColumn);
                String album = cursor.getString(albumColumn);
                String dataStr = cursor.getString(dataColumn);
                Uri data = Uri.parse(dataStr);
                int duration = cursor.getInt(durationColumn);

                Track track = new Track(id, artist, title, album, data, duration);
                tracks.add(track);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(tracks);

        return tracks;
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences.Editor editor = null;
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            editor = prefs.edit();
        }

        return editor;
    }

    public static boolean getRandom(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(RANDOM, false);
    }

    public static void saveRandom(Context context, boolean random) {
        SharedPreferences.Editor editor = getEditor(context);

        if (editor != null) {
            editor.putBoolean(RANDOM, random);
            editor.apply();
        }
    }

    public static Repeat getRepeat(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String repeatString = preferences.getString(REPEAT, Repeat.REPEAT_OFF.toString());
        return Repeat.toRepeat(repeatString);
    }

    public static void saveRepeat(Context context, Repeat repeat) {
        SharedPreferences.Editor editor = getEditor(context);

        if (editor != null) {
            editor.putString(REPEAT, repeat.toString());
            editor.apply();
        }
    }

}
