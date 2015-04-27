package by.itrex.jetfirer.mediaplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class TrackListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private TrackListFragment context;

    private Playlist playlist;

    public TrackListAdapter(LayoutInflater layoutInflater, TrackListFragment context, Playlist playlist) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.playlist = playlist;
    }

    @Override
    public int getCount() {
        return playlist.getTracks().size();
    }

    @Override
    public Object getItem(int position) {
        return playlist.getTracks().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.track_list_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);

        Track track = (Track) getItem(position);
        MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
        if (track != null) {
            title.setText(track.getTitle());
            artist.setText(track.getArtist());
            duration.setText(Utils.convertDuration(track.getDuration()));

            if (mediaPlayerService != null) {
                if (!track.equals(mediaPlayerService.getCurrentTrack())) {
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

        convertView.setTag(track);

        convertView.setOnClickListener(context);

        return convertView;
    }
}
