package by.itrex.jetfirer.mediaplayer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

import java.io.File;
import java.util.ArrayList;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.adapter.PlaylistPagerAdapter;
import by.itrex.jetfirer.mediaplayer.database.DataProvider;
import by.itrex.jetfirer.mediaplayer.fragment.VkFragment;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.util.Utils;
import by.itrex.jetfirer.mediaplayer.widget.MediaPlayerController;
import by.itrex.jetfirer.mediaplayer.widget.MenuItem;


public class MediaPlayerActivity extends FragmentActivity implements View.OnClickListener {

    private static final int CHANGE_BACKGROUND_REQUEST_CODE = 1;
    private static final int CHANGE_PREVIOUS_REQUEST_CODE = 2;
    private static final int CHANGE_STOP_REQUEST_CODE = 3;
    private static final int CHANGE_PLAY_REQUEST_CODE = 4;
    private static final int CHANGE_PAUSE_REQUEST_CODE = 5;
    private static final int CHANGE_NEXT_REQUEST_CODE = 6;
    private static final int CHANGE_RANDOM_REQUEST_CODE = 7;
    private static final int CHANGE_REPEAT_REQUEST_CODE = 8;

    private static final String APP_ID = "4716458";
//    private static final String AUTH_TOKEN = "KskpkvdRc8uZpz4ddTGc";

    private AlertDialog newPlaylistDialog;
    public static Playlist newPlaylist;

    private AlertDialog colorChooser;
    private AlertDialog selectedColorChooser;

    private ResideMenu resideMenu;
    private MenuItem itemBackground;
    private MenuItem itemPrevious;
    private MenuItem itemStop;
    private MenuItem itemPlay;
    private MenuItem itemPause;
    private MenuItem itemNext;
    private MenuItem itemRandom;
    private MenuItem itemRepeat;
    private MenuItem itemColor;
    private MenuItem itemSelectedColor;
    private MenuItem itemReset;

    private LinearLayout root;
    private ImageButton add;
    private ImageButton settings;
    private SearchView searchView;

    private MediaPlayerController mediaPlayerController;

    private ViewPager playlistViewPager;
    private PagerTabStrip playlistPagerTabStrip;
    private PlaylistPagerAdapter playlistPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        VKUIHelper.onCreate(this);

        DataProvider.init(this);

        colorChooser = getColorChooser();
        selectedColorChooser = getSelectedColorChooser();

        mediaPlayerController = (MediaPlayerController) getFragmentManager().findFragmentById(R.id.media_player_controller);

        playlistViewPager = (ViewPager) findViewById(R.id.playlist_view_pager);
        playlistPagerAdapter = new PlaylistPagerAdapter(getSupportFragmentManager(), this);
        playlistViewPager.setAdapter(playlistPagerAdapter);
        playlistViewPager.setOffscreenPageLimit(playlistPagerAdapter.getCount());

        playlistPagerTabStrip = (PagerTabStrip) findViewById(R.id.playlist_pager_tab_strip);
        playlistPagerTabStrip.setTextColor(Utils.getSelectedColor(this));

        setUpMenu();

        root = (LinearLayout) findViewById(R.id.root);
        String path = Utils.getBackgroundImage(this);
        setRootBackground(path);

        searchView = (SearchView) findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);

        add = (ImageButton) findViewById(R.id.add);
        settings = (ImageButton) findViewById(R.id.settings);
        add.setOnClickListener(this);
        settings.setOnClickListener(this);

        initVkSdk();
    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
//        resideMenu.setBackground(R.drawable.main_background);
        resideMenu.attachToActivity(this);
//        resideMenu.setMenuListener(menuListener);
        resideMenu.addIgnoredView(mediaPlayerController.getDurationProgress());
        resideMenu.addIgnoredView(playlistViewPager);
        resideMenu.setScaleValue(0.6f);

        itemBackground = new MenuItem(this, R.drawable.background, "Ground");
        itemPrevious = new MenuItem(this, R.drawable.previous_button_pressed, "Previous");
        itemStop = new MenuItem(this, R.drawable.stop_button_pressed, "Stop");
        itemPlay = new MenuItem(this, R.drawable.play_button_pressed, "Play");
        itemPause = new MenuItem(this, R.drawable.pause_button_pressed, "Pause");
        itemNext = new MenuItem(this, R.drawable.next_button_pressed, "Next");
        itemRandom = new MenuItem(this, R.drawable.random_button, "Random");
        itemRepeat = new MenuItem(this, R.drawable.repeat_button, "Repeat");
        itemColor = new MenuItem(this, R.drawable.color, "Color");
        itemSelectedColor = new MenuItem(this, R.drawable.color, "Select");
        itemReset = new MenuItem(this, R.drawable.reset, "Reset");

        itemBackground.setOnClickListener(this);
        itemPrevious.setOnClickListener(this);
        itemStop.setOnClickListener(this);
        itemPlay.setOnClickListener(this);
        itemPause.setOnClickListener(this);
        itemNext.setOnClickListener(this);
        itemRandom.setOnClickListener(this);
        itemRepeat.setOnClickListener(this);
        itemColor.setOnClickListener(this);
        itemSelectedColor.setOnClickListener(this);
        itemReset.setOnClickListener(this);

        resideMenu.addMenuItem(itemBackground, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemPrevious, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemStop, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemPlay, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemPause, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemNext, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemColor, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSelectedColor, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemReset, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(itemRandom, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(itemRepeat, ResideMenu.DIRECTION_RIGHT);
    }

    private AlertDialog getColorChooser() {
        final ColorPicker colorPicker = new ColorPicker(this);
        int oldColor = Utils.getColor(this);
        colorPicker.setOldCenterColor(oldColor);
        colorPicker.setNewCenterColor(oldColor);
        colorPicker.setColor(oldColor);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(colorPicker)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int color = colorPicker.getColor();
                        colorPicker.setOldCenterColor(color);
                        Utils.saveColor(MediaPlayerActivity.this, color);
                        mediaPlayerController.initTextViews();
                        notifyCurrentList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private AlertDialog getSelectedColorChooser() {
        final ColorPicker colorPicker = new ColorPicker(this);
        int oldColor = Utils.getSelectedColor(this);
        colorPicker.setOldCenterColor(oldColor);
        colorPicker.setNewCenterColor(oldColor);
        colorPicker.setColor(oldColor);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(colorPicker)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int color = colorPicker.getColor();
                        colorPicker.setOldCenterColor(color);
                        Utils.saveSelectedColor(MediaPlayerActivity.this, color);
                        mediaPlayerController.initTextViews();
                        notifyCurrentList();
                        updateMenuItems();
                        playlistPagerTabStrip.setTextColor(Utils.getSelectedColor(MediaPlayerActivity.this));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private void updateMenuItems() {
        int color = Utils.getSelectedColor(this);

        itemBackground.setTextColor(color);
        itemPrevious.setTextColor(color);
        itemStop.setTextColor(color);
        itemPlay.setTextColor(color);
        itemPause.setTextColor(color);
        itemNext.setTextColor(color);
        itemRandom.setTextColor(color);
        itemRepeat.setTextColor(color);
        itemColor.setTextColor(color);
        itemSelectedColor.setTextColor(color);
        itemReset.setTextColor(color);
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
        try {
            if (requestCode == VKSdk.VK_SDK_REQUEST_CODE) {
                VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
                ((VkFragment) playlistPagerAdapter.getItem(PlaylistPagerAdapter.VK_POSITION)).getAudio();
            } else if (requestCode == CHANGE_BACKGROUND_REQUEST_CODE) {
                String path = data.getDataString();
                path = path.substring(path.indexOf(":") + 1);
                setRootBackground(path);
                Utils.saveBackgroundImage(this, path);
            } else if (requestCode == CHANGE_PREVIOUS_REQUEST_CODE) {
                String path = data.getDataString();
                path = path.substring(path.indexOf(":") + 1);
                Utils.savePreviousImage(this, path);
                mediaPlayerController.initButtons();
            } else if (requestCode == CHANGE_STOP_REQUEST_CODE) {
                String path = data.getDataString();
                path = path.substring(path.indexOf(":") + 1);
                Utils.saveStopImage(this, path);
                mediaPlayerController.initButtons();
            } else if (requestCode == CHANGE_PLAY_REQUEST_CODE) {
                String path = data.getDataString();
                path = path.substring(path.indexOf(":") + 1);
                Utils.savePlayImage(this, path);
                mediaPlayerController.initButtons();
            } else if (requestCode == CHANGE_PAUSE_REQUEST_CODE) {
                String path = data.getDataString();
                path = path.substring(path.indexOf(":") + 1);
                Utils.savePauseImage(this, path);
                mediaPlayerController.initButtons();
            } else if (requestCode == CHANGE_NEXT_REQUEST_CODE) {
                String path = data.getDataString();
                path = path.substring(path.indexOf(":") + 1);
                Utils.saveNextImage(this, path);
                mediaPlayerController.initButtons();
            } else if (requestCode == CHANGE_RANDOM_REQUEST_CODE) {

            } else if (requestCode == CHANGE_REPEAT_REQUEST_CODE) {

            }
        } catch (Exception e) {

        }
    }

    private void setRootBackground(String path) {
        try {
            Drawable drawable = getResources().getDrawable(R.drawable.main_background);
            if (path != null) {
                File file = new File(path);
                drawable = Drawable.createFromPath(file.getPath());
            }
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                resideMenu.setBackgroundDrawable(drawable);
                root.setBackgroundDrawable(drawable);
            } else {
                resideMenu.setBackground(drawable);
                root.setBackground(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                addToList();
                break;
            case R.id.settings:
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                break;
        }

        if (v == itemBackground) {
            changeImage(CHANGE_BACKGROUND_REQUEST_CODE);
        } else if (v == itemPrevious) {
            changeImage(CHANGE_PREVIOUS_REQUEST_CODE);
        } else if (v == itemStop) {
            changeImage(CHANGE_STOP_REQUEST_CODE);
        } else if (v == itemPlay) {
            changeImage(CHANGE_PLAY_REQUEST_CODE);
        } else if (v == itemPause) {
            changeImage(CHANGE_PAUSE_REQUEST_CODE);
        } else if (v == itemNext) {
            changeImage(CHANGE_NEXT_REQUEST_CODE);
        } else if (v == itemReset) {
            Utils.clearBackgroundImage(this);
            Utils.clearPreviousImage(this);
            Utils.clearStopImage(this);
            Utils.clearPlayImage(this);
            Utils.clearPauseImage(this);
            Utils.clearNextImage(this);
            Utils.clearColor(this);
            Utils.clearSelectedColor(this);

            setRootBackground(null);
            mediaPlayerController.initButtons();
        } else if (v == itemColor) {
            colorChooser.show();
        } else if (v == itemSelectedColor) {
            selectedColorChooser.show();
        }

        if (v.getId() != R.id.settings) {
            resideMenu.closeMenu();
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

    private void changeImage(int requestCode) {
        Intent intent = new Intent("org.openintents.action.PICK_FILE");
        startActivityForResult(intent, requestCode);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
}
