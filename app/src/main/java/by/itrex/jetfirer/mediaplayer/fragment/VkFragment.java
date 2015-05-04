package by.itrex.jetfirer.mediaplayer.fragment;

import android.net.Uri;
import android.util.Log;

import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VkAudioArray;

import java.util.ArrayList;
import java.util.List;

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
//            VKAccessToken token = VKAccessToken.tokenFromSharedPreferences(getActivity(), Utils.VK_TOKEN_KEY);
            VKSdk.authorize(VKScope.AUDIO);
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
                            Track track = new Track(-1, vkApiAudio.title, vkApiAudio.artist, String.valueOf(vkApiAudio.album_id), Uri.parse(vkApiAudio.url + ".mp3"), vkApiAudio.duration * 1000);
                            tracks.add(track);
                            Log.i("Test", track.toString());
                        }
                        Playlist playlist = new Playlist(-1, PlaylistFragment.VK_FRAGMENT_NAME, tracks);
                        setPlaylist(playlist);
                        trackListAdapter.setPlaylist(playlist);
                        trackListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
