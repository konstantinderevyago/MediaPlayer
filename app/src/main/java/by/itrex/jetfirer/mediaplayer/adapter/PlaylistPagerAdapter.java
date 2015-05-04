package by.itrex.jetfirer.mediaplayer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.database.DataProvider;
import by.itrex.jetfirer.mediaplayer.fragment.PlaylistFragment;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.fragment.VkFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class PlaylistPagerAdapter extends FragmentStatePagerAdapter {

    public static int DEFAULT_PLAYLIST_POSITION = 0;
    public static int PLAYLISTs_POSITION = 1;
    public static int FOLDERS_POSITION = 2;
    public static int ARTISTS_POSITION = 3;
    public static int ALBUMS_POSITION = 4;
    public static int VK_POSITION = 5;

    private FragmentManager fragmentManager;
    private Context context;

    private List<List<TrackListFragment>> trackListFragments = new ArrayList<>();

    public PlaylistPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;

        trackListFragments = new ArrayList<>();

        List<TrackListFragment> defaultPlaylist = new ArrayList<>();
        defaultPlaylist.add(TrackListFragment.getInstance(Utils.getDefaultPlaylist(context)));
        trackListFragments.add(defaultPlaylist);

        List<TrackListFragment> playlists = new ArrayList<>();
        playlists.add(PlaylistFragment.getInstance(DataProvider.getAllPlaylists(), PlaylistFragment.PLAYLIST_FRAGMENT_NAME));
        trackListFragments.add(playlists);

        List<TrackListFragment> folders = new ArrayList<>();
        folders.add(PlaylistFragment.getInstance(Utils.getFolders(context), PlaylistFragment.FOLDER_FRAGMENT_NAME));
        trackListFragments.add(folders);

        List<TrackListFragment> artists = new ArrayList<>();
        artists.add(PlaylistFragment.getInstance(Utils.getArtists(context), PlaylistFragment.ARTIST_FRAGMENT_NAME));
        trackListFragments.add(artists);

        List<TrackListFragment> albums = new ArrayList<>();
        albums.add(PlaylistFragment.getInstance(Utils.getAlbums(context), PlaylistFragment.ALBUM_FRAGMENT_NAME));
        trackListFragments.add(albums);

        List<TrackListFragment> vk = new ArrayList<>();
        vk.add(VkFragment.getInstance(new Playlist(-1, PlaylistFragment.VK_FRAGMENT_NAME, new ArrayList<Track>())));
        trackListFragments.add(vk);
    }

    @Override
    public Fragment getItem(int position) {
        List<TrackListFragment> list = trackListFragments.get(position);
        return list.get(list.size() - 1);
    }

    @Override
    public int getCount() {
        return trackListFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((TrackListFragment) getItem(position)).getName();
    }

    public void notifyCurrentList(int position) {
        ((TrackListFragment) getItem(position)).notifyDataSetChanged();
    }

    public void openPlaylist(int position, Playlist playlist) {
        TrackListFragment trackListFragment = TrackListFragment.getInstance(playlist);
        trackListFragments.get(position).add(trackListFragment);
        notifyDataSetChanged();
    }

    public boolean closePlaylist(int position) {
        List<TrackListFragment> list = trackListFragments.get(position);
        if (list.size() > 1) {
            list.remove(list.size() - 1);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }
}
