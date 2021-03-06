package by.itrex.jetfirer.mediaplayer.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VkAudioArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;

/**
 * Created by Konstantin on 04.05.2015.
 */
public class VkFragment extends TrackListFragment {

    public static VkFragment getInstance(Playlist playlist) {
        VkFragment vkFragment = new VkFragment();
        vkFragment.setPlaylist(playlist);
        return vkFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            authorize();
        }
    }

    public void update() {
        VKSdk.logout();
        authorize();
    }

    private void authorize() {
        VKSdk.authorize(VKScope.AUDIO);
        getAudio();
    }

    public void getAudio() {
        if (VKSdk.wakeUpSession()) {
            VKRequest vkRequest = VKApi.audio().get();
            vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VkAudioArray vkAudioArray = (VkAudioArray) response.parsedModel;
                    List<Track> tracks = new ArrayList<>();
                    for (int i = 0; i < vkAudioArray.getCount(); i++) {
                        VKApiAudio vkApiAudio = vkAudioArray.get(i);
                        Track track = new Track(-1, vkApiAudio.title, vkApiAudio.artist, String.valueOf(vkApiAudio.album_id), vkApiAudio.url, vkApiAudio.duration * 1000);
                        tracks.add(track);
                    }
                    Playlist playlist = new Playlist(-1, PlaylistFragment.VK_FRAGMENT_NAME, tracks);
                    setPlaylist(playlist);
                    trackListAdapter.setPlaylist(playlist);
                    trackListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void downloadTrack(Track track) {
        try {
            if (track != null && track.getData() != null) {
                File file = new File("/storage/sdcard0/" + track.getArtist() + " - " + track.getTitle());
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final LinearLayout settingsLayout = (LinearLayout) v.findViewById(R.id.settings_layout);
        if (settingsLayout.getVisibility() != View.VISIBLE) {
            settingsLayout.setVisibility(View.VISIBLE);
            ImageView download = (ImageView) settingsLayout.findViewById(R.id.download);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadTrack((Track) v.getTag());
                    settingsLayout.setVisibility(View.GONE);
                }
            });
        } else {
            settingsLayout.setVisibility(View.GONE);
        }
        return super.onLongClick(v);
    }
}
