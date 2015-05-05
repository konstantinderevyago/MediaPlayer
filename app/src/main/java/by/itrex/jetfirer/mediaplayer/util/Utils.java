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

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.enums.Repeat;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class Utils {

    public static final String RANDOM = "RANDOM";
    public static final String REPEAT = "REPEAT";
//    public static final String VK_TOKEN_KEY = "VK_SDK_ACCESS_TOKEN_PLEASE_DONT_TOUCH";

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

    public static Long stringToLong(String str) {
        long value = 0;

        if (str != null) {
            byte[] bytes = str.getBytes();
            for (byte aByte : bytes) {
                value += (value << 8) + (aByte & 0xff);
            }
            if (value < 0) {
                value *= -1;
            }
        }

        return value;
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
                String data = cursor.getString(dataColumn);
                int duration = cursor.getInt(durationColumn);

                Track track = new Track(id, title, artist, album, data, duration);
                tracks.add(track);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        Collections.sort(tracks);

        return tracks;
    }

    public static Playlist getDefaultPlaylist(Context context) {
        Playlist playlist = new Playlist();
        playlist.setId((long) -1);
        playlist.setName(context.getString(R.string.all_tracks));
        playlist.setTracks(Utils.getAllTracks(context));
        return  playlist;
    }

    public static List<Playlist> getArtists(Context context) {
        List<Playlist> playlists = new ArrayList<>();

        Playlist defaultPlaylist = getDefaultPlaylist(context);
        for (Track track : defaultPlaylist.getTracks()) {
            boolean exist = false;
            for (Playlist playlist : playlists) {
                if (playlist.getName().equals(track.getArtist())) {
                    playlist.getTracks().add(track);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                Playlist playlist = new Playlist();
                playlist.setName(track.getArtist());
                List<Track> tracks = new ArrayList<>();
                tracks.add(track);
                playlist.setTracks(tracks);
                playlist.setId(stringToLong(playlist.getName()));

                playlists.add(playlist);
            }
        }

        return playlists;
    }

    public static List<Playlist> getAlbums(Context context) {
        List<Playlist> playlists = new ArrayList<>();

        Playlist defaultPlaylist = getDefaultPlaylist(context);
        for (Track track : defaultPlaylist.getTracks()) {
            boolean exist = false;
            for (Playlist playlist : playlists) {
                if (playlist.getName().equals(track.getAlbum()) && playlist.getTracks().get(0).getArtist().equals(track.getArtist())) {
                    playlist.getTracks().add(track);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                Playlist playlist = new Playlist();
                playlist.setName(track.getAlbum());
                List<Track> tracks = new ArrayList<>();
                tracks.add(track);
                playlist.setTracks(tracks);
                playlist.setId(stringToLong(playlist.getName()));

                playlists.add(playlist);
            }
        }

        return playlists;
    }

    public static List<Playlist> getFolders(Context context) {
        List<Playlist> playlists = new ArrayList<>();

        Playlist defaultPlaylist = getDefaultPlaylist(context);
        for (Track track : defaultPlaylist.getTracks()) {
            String data = track.getData();
            String[] folders = data.split("/");
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 1; i < folders.length - 1; i++) {
                String folder = folders[i];
                pathBuilder.append("/").append(folder);
                String path = pathBuilder.toString();

                boolean exist = false;
                for (Playlist playlist : playlists) {
                    if (path.equals(playlist.getName())) {
                        exist = true;
                        playlist.getTracks().add(track);
                        break;
                    }
                }
                if (!exist) {
                    Playlist playlist = new Playlist();
                    playlist.setName(path);
                    List<Track> tracks = new ArrayList<>();
                    tracks.add(track);
                    playlist.setTracks(tracks);
                    playlist.setId(stringToLong(path));

                    playlists.add(playlist);
                }
            }
        }

        List<Playlist> forRemove = new ArrayList<>();
        for (Playlist playlist : playlists) {

            String data = playlist.getName();
            String[] names = data.split("/");
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 1; i < names.length - 1; i++) {
                pathBuilder.append("/").append(names[i]);
            }
            String path = pathBuilder.toString();

            for (Playlist p : playlists) {
                if (path.equals(p.getName()) && playlist.getTracks().equals(p.getTracks())) {
                    forRemove.add(p);
                }
            }
        }

        playlists.removeAll(forRemove);

        for (Playlist playlist : playlists) {
            String folder = playlist.getName();
            String[] folders = folder.split("/");
            if (folders.length > 0) {
                folder = "/" + folders[folders.length - 1];
            }
            playlist.setName(folder);
        }

        return playlists;
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
