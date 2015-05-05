package by.itrex.jetfirer.mediaplayer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VkAudioArray;
import com.vk.sdk.util.VKUtil;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.adapter.PlaylistPagerAdapter;
import by.itrex.jetfirer.mediaplayer.database.DataProvider;
import by.itrex.jetfirer.mediaplayer.fragment.VkFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.util.Utils;
import by.itrex.jetfirer.mediaplayer.widget.MediaPlayerController;


public class MediaPlayerActivity extends FragmentActivity implements View.OnClickListener {

    private static final String APP_ID = "4716458";
//    private static final String AUTH_TOKEN = "KskpkvdRc8uZpz4ddTGc";

    private ImageButton add;
    private SearchView searchView;

    private MediaPlayerController mediaPlayerController;

    private ViewPager playlistViewPager;
    private PlaylistPagerAdapter playlistPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        VKUIHelper.onCreate(this);

        DataProvider.init(this);

        searchView = (SearchView) findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);

        add = (ImageButton) findViewById(R.id.add);
        add.setOnClickListener(this);

        mediaPlayerController = (MediaPlayerController) getFragmentManager().findFragmentById(R.id.media_player_controller);

        playlistViewPager = (ViewPager) findViewById(R.id.playlist_view_pager);
        playlistPagerAdapter = new PlaylistPagerAdapter(getSupportFragmentManager(), this);
        playlistViewPager.setAdapter(playlistPagerAdapter);
        playlistViewPager.setOffscreenPageLimit(playlistPagerAdapter.getCount());

        initVkSdk();
    }

    private void initVkSdk() {
        VKSdk.initialize(new VKSdkListener() {
            @Override
            public void onCaptchaError(VKError vkError) {

            }

            @Override
            public void onTokenExpired(VKAccessToken vkAccessToken) {

            }

            @Override
            public void onAccessDenied(VKError vkError) {

            }
        }, APP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        ((VkFragment) playlistPagerAdapter.getItem(PlaylistPagerAdapter.VK_POSITION)).getAudio();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                addToList();
                break;
        }
    }

    private void addToList() {
        int position = playlistViewPager.getCurrentItem();
        if (position == PlaylistPagerAdapter.PLAYLISTS_POSITION) {
            DataProvider.dropData();
//            DataProvider.test();
            DataProvider.saveOrUpdatePlaylist(Utils.getDefaultPlaylist(this));
            notifyCurrentList();
//            DataProvider.test();
        } else if (position == PlaylistPagerAdapter.VK_POSITION) {
            ((VkFragment) playlistPagerAdapter.getItem(PlaylistPagerAdapter.VK_POSITION)).update();
        }
    }

    @Override
    public void onBackPressed() {
        if (!playlistPagerAdapter.closePlaylist(playlistViewPager.getCurrentItem())) {
            super.onBackPressed();
        }
    }

    public void startTrack(Playlist playlist, Track track) {
        mediaPlayerController.initPlaylist(playlist, track);
    }

    public void notifyCurrentList() {
        playlistPagerAdapter.notifyCurrentList(playlistViewPager.getCurrentItem());
    }

    public void openPlaylist(Playlist playlist) {
        playlistPagerAdapter.openPlaylist(playlistViewPager.getCurrentItem(), playlist);
    }

}
