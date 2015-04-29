package by.itrex.jetfirer.mediaplayer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.adapter.PlaylistPagerAdapter;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.widget.MediaPlayerController;


public class MediaPlayerActivity extends FragmentActivity {

    private MediaPlayerController mediaPlayerController;

    private ViewPager playlistViewPager;
    private PlaylistPagerAdapter playlistPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        mediaPlayerController = (MediaPlayerController) getFragmentManager().findFragmentById(R.id.media_player_controller);

        playlistViewPager = (ViewPager) findViewById(R.id.playlist_view_pager);
        playlistPagerAdapter = new PlaylistPagerAdapter(getSupportFragmentManager(), this);
        playlistViewPager.setAdapter(playlistPagerAdapter);
        playlistViewPager.setOffscreenPageLimit(playlistPagerAdapter.getCount());
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
