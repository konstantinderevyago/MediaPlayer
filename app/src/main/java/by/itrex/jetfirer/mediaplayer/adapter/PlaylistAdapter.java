package by.itrex.jetfirer.mediaplayer.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.activity.MediaPlayerActivity;
import by.itrex.jetfirer.mediaplayer.fragment.TrackListFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;
import by.itrex.jetfirer.mediaplayer.util.Utils;

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
        CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
        check.setButtonDrawable(id);
        check.setOnClickListener(context);

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
                    int color = Utils.getColor(context.getActivity());
                    title.setTextColor(color);
                    artist.setTextColor(color);
                    duration.setTextColor(color);
                } else {
//                    int color = 0xFF0030FF;
//                    int color = 0xFF33B5E5;
                    int color = Utils.getSelectedColor(context.getActivity());
                    title.setTextColor(color);
                    artist.setTextColor(color);
                    duration.setTextColor(color);
                }
            }
        }

        if (MediaPlayerActivity.newPlaylist != null) {
            check.setVisibility(View.VISIBLE);
            check.setChecked(MediaPlayerActivity.newPlaylist.getTracks().containsAll(playlist.getTracks()));
        } else {
            check.setVisibility(View.GONE);
        }

        convertView.setTag(playlist);
        check.setTag(playlist);

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
