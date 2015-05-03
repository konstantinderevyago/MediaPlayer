package by.itrex.jetfirer.mediaplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.activity.MediaPlayerActivity;
import by.itrex.jetfirer.mediaplayer.adapter.PlaylistAdapter;
import by.itrex.jetfirer.mediaplayer.database.DataProvider;
import by.itrex.jetfirer.mediaplayer.model.Playlist;

/**
 * Created by Konstantin on 29.04.2015.
 */
public class PlaylistFragment extends TrackListFragment {

    public static final String PLAYLIST_FRAGMENT_NAME = "Playlists";
    public static final String FOLDER_FRAGMENT_NAME = "Folders";
    public static final String ARTIST_FRAGMENT_NAME = "Artists";
    public static final String ALBUM_FRAGMENT_NAME = "Albums";
    public static final String VK_FRAGMENT_NAME = "VK";
    public static final String FACEBOOK_FRAGMENT_NAME = "Facebook";

    private List<Playlist> playlists;

    private String name;

    public static PlaylistFragment getInstance(List<Playlist> playlists, String name) {
        PlaylistFragment playlistFragment = new PlaylistFragment();
        playlistFragment.setPlaylists(playlists);
        playlistFragment.setName(name);
        return playlistFragment;
    }

    public static PlaylistFragment getInstance(Playlist playlist) {
        PlaylistFragment playlistFragment = new PlaylistFragment();
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        playlistFragment.setPlaylists(playlists);
        playlistFragment.setName(playlist.getName());
        return playlistFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_list, null);

        trackList = (ListView) view.findViewById(R.id.track_list);

        trackListAdapter = new PlaylistAdapter(inflater, this, playlists);
        trackList.setAdapter(trackListAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Playlist playlist = (Playlist) v.getTag();

        ((MediaPlayerActivity) getActivity()).openPlaylist(playlist);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        if (PLAYLIST_FRAGMENT_NAME.equals(name)) {
            playlists = DataProvider.getAllPlaylists();
            ((PlaylistAdapter) trackListAdapter).setPlaylists(playlists);
            trackListAdapter.notifyDataSetChanged();
        }
    }

    public String getName() {
        return name;
    }

    private void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    private void setName(String name) {
        this.name = name;
    }
}
