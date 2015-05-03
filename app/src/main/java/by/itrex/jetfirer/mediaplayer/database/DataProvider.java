package by.itrex.jetfirer.mediaplayer.database;

import android.content.Context;

import java.util.List;

import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;

/**
 * Created by Konstantin on 17.12.2014.
 */
public class DataProvider {

    private static DbService dbService;
    private static Context context;

    private DataProvider() {}

    public static void init(Context c){
        dbService = new DbService(c);
        context = c;
    }

    public static void closeDatabase() {
        dbService.close();
    }


    public static void dropData() {
        dbService.dropData();
    }

    public static List<Track> getPlaylistTracks(long playlistId) {
        return dbService.getPlaylistTracks(playlistId);
    }

    public static List<Playlist> getAllPlaylists() {
        return dbService.getAllPlaylists();
    }

    public static void saveOrUpdatePlaylist(Playlist playlist) {
        dbService.saveOrUpdatePlaylist(playlist);
    }

    public static void deletePlaylist(long id) {
        dbService.deletePlaylist(id);
    }

    public static void test() {
        dbService.test();
    }

}
