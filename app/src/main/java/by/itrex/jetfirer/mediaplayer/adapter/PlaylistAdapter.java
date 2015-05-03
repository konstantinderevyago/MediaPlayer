package by.itrex.jetfirer.mediaplayer.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;

/**
 * Created by Konstantin on 29.04.2015.
 */
public class PlaylistAdapter extends TrackListAdapter {

    private List<Playlist> playlists;

    public PlaylistAdapter(LayoutInflater layoutInflater, TrackListFragment context, List<Playlist> playlists) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int position) {
        return playlists.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.track_list_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);

        Playlist playlist = (Playlist) getItem(position);
        if (playlist != null) {
            title.setText(playlist.getName());

            int tracksCount = playlist.getTracks().size();
            String tracksCountStr = tracksCount + " track";
            if (tracksCount > 1) {
                tracksCountStr = tracksCountStr + "s";
            }
            artist.setText(tracksCountStr);
            duration.setText("");

            MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
            if (mediaPlayerService != null) {
                if (!playlist.equals(mediaPlayerService.getPlaylist())) {
                    title.setTextColor(Color.WHITE);
                    artist.setTextColor(Color.WHITE);
                    duration.setTextColor(Color.WHITE);
                } else {
                    int color = 0xFF0030FF;
                    title.setTextColor(color);
                    artist.setTextColor(color);
                    duration.setTextColor(color);
                }
            }
        }

        convertView.setTag(playlist);

        convertView.setOnClickListener(context);

        return convertView;
    }

    private int getItemPosition(Playlist playlist) {
        for (int i = 0; i < playlists.size(); i++) {
            Playlist p = playlists.get(i);
            if (p.equals(playlist)) {
                return i;
            }
        }
        return -1;
    }

    public int getCurrentItemPosition() {
        MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
        if (mediaPlayerService != null) {
            return getItemPosition(mediaPlayerService.getPlaylist());
        }

        return -1;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
