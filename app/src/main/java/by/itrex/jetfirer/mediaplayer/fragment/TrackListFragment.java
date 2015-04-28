package by.itrex.jetfirer.mediaplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.activity.MediaPlayerActivity;
import by.itrex.jetfirer.mediaplayer.adapter.TrackListAdapter;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class TrackListFragment extends Fragment implements View.OnClickListener {

    private ListView trackList;
    private TrackListAdapter trackListAdapter;

    private Playlist playlist;

    public static TrackListFragment getInstance(Playlist playlist) {
        TrackListFragment trackListFragment = new TrackListFragment();
        trackListFragment.setPlaylist(playlist);
        return trackListFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_list, null);

        trackList = (ListView) view.findViewById(R.id.track_list);

        trackListAdapter = new TrackListAdapter(inflater, this, playlist);
        trackList.setAdapter(trackListAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Track track = (Track) v.getTag();

        ((MediaPlayerActivity) getActivity()).startTrack(playlist, track);

        trackListAdapter.notifyDataSetChanged();
    }

    public String getName() {
        if (playlist != null) {
            return playlist.getName();
        }

        return null;
    }

    public void notifyDataSetChanged() {
        if (trackListAdapter != null) {
            trackListAdapter.notifyDataSetChanged();

            int position = trackListAdapter.getCurrentTrackPosition();
            if (position != -1) {
                trackList.smoothScrollToPosition(position);
            }
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
