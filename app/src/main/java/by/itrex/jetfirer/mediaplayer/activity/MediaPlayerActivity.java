package by.itrex.jetfirer.mediaplayer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.adapter.PlaylistPagerAdapter;
import by.itrex.jetfirer.mediaplayer.database.DataProvider;
import by.itrex.jetfirer.mediaplayer.fragment.VkFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.widget.MediaPlayerController;


public class MediaPlayerActivity extends FragmentActivity implements View.OnClickListener {

    private static final String APP_ID = "4716458";
//    private static final String AUTH_TOKEN = "KskpkvdRc8uZpz4ddTGc";

    private AlertDialog newPlaylistDialog;
    public static Playlist newPlaylist;

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
        if (newPlaylist != null) {
            if (!newPlaylist.getTracks().isEmpty()) {
                DataProvider.saveOrUpdatePlaylist(newPlaylist);
            }
            add.setImageResource(R.drawable.button_add_selector);
            newPlaylist = null;
            notifyCurrentList();
        } else if (position == PlaylistPagerAdapter.PLAYLISTS_POSITION) {
                showNewPlaylistDialog();
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

    private void showNewPlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_new_playlist, null);
        final EditText playlistNameInput = (EditText) view.findViewById(R.id.playlist_name);

        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = String.valueOf(playlistNameInput.getText());
                if (!name.equals("")) {
                    add.setImageResource(R.drawable.button_ok_selector);
                    newPlaylist = new Playlist();
                    newPlaylist.setName(name);
                    newPlaylist.setTracks(new ArrayList<Track>());
                    notifyCurrentList();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newPlaylistDialog.dismiss();
            }
        });
        newPlaylistDialog = builder.create();
        newPlaylistDialog.show();
    }

}
