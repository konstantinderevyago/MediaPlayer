package by.itrex.jetfirer.mediaplayer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.fragment.PlaylistFragment;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class PlaylistPagerAdapter extends FragmentStatePagerAdapter {

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

        List<TrackListFragment> folders = new ArrayList<>();
        folders.add(PlaylistFragment.getInstance(Utils.getFolders(context), PlaylistFragment.FOLDER_FRAGMENT_NAME));
        trackListFragments.add(folders);

        List<TrackListFragment> artists = new ArrayList<>();
        artists.add(PlaylistFragment.getInstance(Utils.getArtists(context), PlaylistFragment.ARTIST_FRAGMENT_NAME));
        trackListFragments.add(artists);

        List<TrackListFragment> albums = new ArrayList<>();
        albums.add(PlaylistFragment.getInstance(Utils.getAlbums(context), PlaylistFragment.ALBUM_FRAGMENT_NAME));
        trackListFragments.add(albums);
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
        List<TrackListFragment> list = trackListFragments.get(position);
        list.get(list.size() - 1).notifyDataSetChanged();
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
