package by.itrex.jetfirer.mediaplayer.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.adapter.PlaylistPagerAdapter;
import by.itrex.jetfirer.mediaplayer.database.DataProvider;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.util.Utils;
import by.itrex.jetfirer.mediaplayer.widget.MediaPlayerController;


public class MediaPlayerActivity extends FragmentActivity implements View.OnClickListener {

    private ImageButton add;
    private SearchView searchView;

    private MediaPlayerController mediaPlayerController;

    private ViewPager playlistViewPager;
    private PlaylistPagerAdapter playlistPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

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
        DataProvider.dropData();
        DataProvider.test();
        DataProvider.saveOrUpdatePlaylist(Utils.getDefaultPlaylist(this));
        notifyCurrentList();
        DataProvider.test();
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
