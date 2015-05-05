package by.itrex.jetfirer.mediaplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;

import static by.itrex.jetfirer.mediaplayer.database.TableConstants.ALBUM;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.ARTIST;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.DATA;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.DELETE_FROM;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.DURATION;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.NAME;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.PLAYLIST_ID;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.PLAYLIST_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.PLAYLIST_TRACK_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.TITLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.TRACK_ID;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.TRACK_TABLE;

/**
 * Created by Konstantin on 17.12.2014.
 */
public class DbService {

    private static final String[] TRACK_COLUMNS = new String[]{"_id", ARTIST, TITLE, DATA, DURATION};
    private static final String[] PLAYLIST_COLUMNS = new String[]{"_id", NAME};
    private static final String[] PLAYLIST_TRACK_COLUMNS = new String[]{"_id", TRACK_ID, PLAYLIST_ID};

    private SQLiteDatabase database;
    private Context context;

    public DbService(Context context) {
        DbHelper helper = new DbHelper(context);
        this.context = context;
        database = helper.getWritableDatabase();
    }

    public void dropData() {
        database.execSQL(DELETE_FROM + TRACK_TABLE);
        database.execSQL(DELETE_FROM + PLAYLIST_TABLE);
        database.execSQL(DELETE_FROM + PLAYLIST_TRACK_TABLE);
    }

    private long insertOrUpdate(String table, ContentValues values, String clause) {
        int rowNumber;
        rowNumber = database.update(table, values, clause, null);
        if (rowNumber == 0) {
            return database.insert(table, null, values);
        }

        return -1;
    }

    public void close() {
        database.close();
    }

    private ContentValues getPlaylistContentValues(Playlist playlist) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, playlist.getName());
        return contentValues;
    }

    private ContentValues getTrackContentValues(Track track) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTIST, track.getArtist());
        contentValues.put(TITLE, track.getTitle());
        contentValues.put(ALBUM, track.getAlbum());
        contentValues.put(DATA, track.getData());
        contentValues.put(DURATION, track.getDuration());
        return contentValues;
    }

    private ContentValues getPlaylistTrackContentValues(Playlist playlist, Track track) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYLIST_ID, playlist.getId());
        contentValues.put(TRACK_ID, track.getId());
        return contentValues;
    }

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();

        Cursor cursor = database.query(PLAYLIST_TABLE, PLAYLIST_COLUMNS, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String name = cursor.getString(1);
            playlists.add(new Playlist(id, name, getPlaylistTracks(id)));
        }

        cursor.close();

        return playlists;
    }

    public List<Track> getPlaylistTracks(long playlistId) {
        List<Track> tracks = new ArrayList<>();

        String query = "SELECT t._id, t." + ARTIST + ", t." + TITLE + ", t." + ALBUM + ", t." + DATA + ", t." + DURATION + " FROM " + TRACK_TABLE
                + " AS t INNER JOIN " + PLAYLIST_TRACK_TABLE + " AS pt ON t._id = pt." + TRACK_ID + " WHERE pt." + PLAYLIST_ID + " = " + playlistId;

        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String artist = cursor.getString(1);
            String title = cursor.getString(2);
            String album = cursor.getString(3);
            String data = cursor.getString(4);
            Integer duration = cursor.getInt(5);

            Track track = new Track(id, title, artist, album, data, duration);
            tracks.add(track);
        }

        cursor.close();

        return tracks;
    }

    public void saveOrUpdatePlaylist(Playlist playlist) {
        try {
            database.beginTransaction();

            String clause = null;
            if (playlist.getId() != -1) {
                clause = "_id = " + playlist.getId();
            }
            playlist.setId(insertOrUpdate(PLAYLIST_TABLE, getPlaylistContentValues(playlist), clause));

            for (Track track : playlist.getTracks()) {
                long trackId = track.getId();
                String trackClause = null;
                if (trackId != -1) {
                    trackClause = "_id = " + trackId;
                }
                track.setId(insertOrUpdate(TRACK_TABLE, getTrackContentValues(track), trackClause));
                insertOrUpdate(PLAYLIST_TRACK_TABLE, getPlaylistTrackContentValues(playlist, track),
                        PLAYLIST_ID + " = " + playlist.getId() + " AND " + TRACK_ID + " = " + track.getId());
            }

            database.setTransactionSuccessful();
        }finally {
            database.endTransaction();
        }
    }

    public void deletePlaylist(long id) {
        String clause = " = '" + id + "'";
        database.delete(PLAYLIST_TRACK_TABLE, PLAYLIST_ID + clause, null);
        database.delete(PLAYLIST_TABLE, "_id" + clause, null);
    }

    public void test() {
        List<Track> tracks = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TRACK_TABLE, null);

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String artist = cursor.getString(1);
            String title = cursor.getString(2);
            String album = cursor.getString(3);
            String data = cursor.getString(4);
            Integer duration = cursor.getInt(5);

            Track track = new Track(id, title, artist, album, data, duration);
            tracks.add(track);
            Log.i("Test", track.toString());
        }

        Cursor cursor2 = database.rawQuery("SELECT " + PLAYLIST_ID + ", " + TRACK_ID + " FROM " + PLAYLIST_TRACK_TABLE, null);
        int count = 0;
        while (cursor2.moveToNext()) {
            Long id = cursor2.getLong(0);
            Long id2 = cursor2.getLong(1);
            Log.i("Test", "playlist_id = " + id);
            Log.i("Test", "track_id = " + id2);
            count++;
        }

        Log.i("Test", "--------------");
        Log.i("Test", tracks.size() + " | " + count);

        cursor2.close();
    }

}
