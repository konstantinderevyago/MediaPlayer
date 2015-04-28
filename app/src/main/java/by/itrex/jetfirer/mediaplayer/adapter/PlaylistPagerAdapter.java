package by.itrex.jetfirer.mediaplayer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class PlaylistPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private Context context;

    private List<List<TrackListFragment>> trackListFragments = new ArrayList<>();

    public PlaylistPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;

        trackListFragments = new ArrayList<>();
        List<TrackListFragment> list = new ArrayList<>();
        list.add(TrackListFragment.getInstance(getDefaultPlaylist()));
        trackListFragments.add(list);
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
    public CharSequence getPageTitle(int position) {
        List<TrackListFragment> list = trackListFragments.get(position);
        return list.get(list.size() - 1).getName();
    }

    public void notifyCurrentList(int position) {
        List<TrackListFragment> list = trackListFragments.get(position);
        list.get(list.size() - 1).notifyDataSetChanged();
    }

    private Playlist getDefaultPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId((long) -1);
        playlist.setName(context.getString(R.string.all_tracks));
        playlist.setTracks(Utils.getAllTracks(context));
        return  playlist;
    }
}
