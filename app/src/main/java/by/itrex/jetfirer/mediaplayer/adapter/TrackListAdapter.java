package by.itrex.jetfirer.mediaplayer.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.activity.MediaPlayerActivity;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 27.04.2015.
 */
public class TrackListAdapter extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    protected TrackListFragment context;

    private Playlist playlist;

    protected TrackListAdapter() {

    }

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
        CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        check.setButtonDrawable(id);
        check.setOnClickListener(context);

        Track track = (Track) getItem(position);
        if (track != null) {
            title.setText(track.getTitle());
            artist.setText(track.getArtist());
            duration.setText(Utils.convertDuration(track.getDuration()));

            MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
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

        if (MediaPlayerActivity.newPlaylist != null) {
            check.setVisibility(View.VISIBLE);
            check.setChecked(MediaPlayerActivity.newPlaylist.getTracks().contains(track));
        } else {
            check.setVisibility(View.GONE);
        }

        convertView.setTag(track);
        check.setTag(track);

        convertView.setOnClickListener(context);

        return convertView;
    }

    private int getItemPosition(Track track) {
        List<Track> tracks = playlist.getTracks();
        for (int i = 0; i < tracks.size(); i++) {
            Track t = tracks.get(i);
            if (t.equals(track)) {
                return i;
            }
        }
        return -1;
    }

    public int getCurrentItemPosition() {
        MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
        if (mediaPlayerService != null) {
            return getItemPosition(mediaPlayerService.getCurrentTrack());
        }

        return -1;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
