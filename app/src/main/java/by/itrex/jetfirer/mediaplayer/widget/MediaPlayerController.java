package by.itrex.jetfirer.mediaplayer.widget;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.activity.MediaPlayerActivity;
import by.itrex.jetfirer.mediaplayer.enums.Repeat;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;
import by.itrex.jetfirer.mediaplayer.service.TrackCompletedListener;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 24.04.2015.
 */
public class MediaPlayerController extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, TrackCompletedListener {

    private MediaPlayerService mediaPlayerService;

    private SeekBarHandler seekBarHandler;
    private boolean isViewOn = false;

    private TextView playingTitle;
    private TextView playingArtist;
    private TextView playingDuration;
    private TextView maxDuration;
    private ImageButton randomButton;
    private ImageButton repeatButton;
    private ImageButton previousButton;
    private ImageButton stopButton;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;
    private SeekBar durationProgress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_player_controller, null);

        playingTitle = (TextView) view.findViewById(R.id.playing_title);
        playingArtist = (TextView) view.findViewById(R.id.playing_artist);
        playingDuration = (TextView) view.findViewById(R.id.playing_duration);
        maxDuration = (TextView) view.findViewById(R.id.max_duration);
        randomButton = (ImageButton) view.findViewById(R.id.random_button);
        repeatButton = (ImageButton) view.findViewById(R.id.repeat_button);
        previousButton = (ImageButton) view.findViewById(R.id.previous_button);
        stopButton = (ImageButton) view.findViewById(R.id.stop_button);
        playButton = (ImageButton) view.findViewById(R.id.play_button);
        pauseButton = (ImageButton) view.findViewById(R.id.pause_button);
        nextButton = (ImageButton) view.findViewById(R.id.next_button);
        durationProgress = (SeekBar) view.findViewById(R.id.track_duration_progress);

        randomButton.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        durationProgress.setOnSeekBarChangeListener(this);

        initTextViews();
        initButtons();

        initMediaPlayerService();

        return view;
    }

    public void initTextViews() {
        int color = Utils.getColor(getActivity());
        playingTitle.setTextColor(color);
        playingArtist.setTextColor(color);
        playingDuration.setTextColor(color);
        maxDuration.setTextColor(color);
    }

    public void initButtons() {
        setImage(previousButton, Utils.getPreviousImage(getActivity()), R.drawable.button_previous_selector);
        setImage(stopButton, Utils.getStopImage(getActivity()), R.drawable.button_stop_selector);
        setImage(playButton, Utils.getPlayImage(getActivity()), R.drawable.button_play_selector);
        setImage(pauseButton, Utils.getPauseImage(getActivity()), R.drawable.button_pause_selector);
        setImage(nextButton, Utils.getNextImage(getActivity()), R.drawable.button_next_selector);
    }

    private void setImage(ImageButton button, String path, int defaultResouce) {
        try {
            Drawable drawable = getResources().getDrawable(defaultResouce);
            if (path != null) {
                File file = new File(path);
                drawable = Drawable.createFromPath(file.getPath());
            }
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable(drawable);
            } else {
                button.setBackground(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMediaPlayerService() {
        mediaPlayerService = MediaPlayerService.getInstance();
        mediaPlayerService.setTrackCompletedListener(this);

        if (!mediaPlayerService.isPlaying()) {
            getActivity().startService(new Intent(getActivity(), MediaPlayerService.class));
        }
    }

    private void setPlayingInfo() {
        Track track = mediaPlayerService.getCurrentTrack();
        if (track != null) {
            showPlayingTrackInfo(track);
        }

        setRandom(Utils.getRandom(getActivity()));
        setRepeat(Utils.getRepeat(getActivity()));
    }

    private void setRandom(boolean random) {
        if (random) {
            randomButton.setImageResource(R.drawable.random_button_pressed);
        } else {
            randomButton.setImageResource(R.drawable.random_button);
        }
        mediaPlayerService.setRandom(random);
        Utils.saveRandom(getActivity(), random);
    }

    private void setRepeat(Repeat repeat) {
        switch (repeat) {
            case REPEAT_OFF:
                repeatButton.setImageResource(R.drawable.repeat_button);
                break;
            case REPEAT_ALL:
                repeatButton.setImageResource(R.drawable.repeat_button_pressed);
                break;
            case REPEAT_SINGLE:
                repeatButton.setImageResource(R.drawable.repeat_button_single_pressed);
                break;
        }
        mediaPlayerService.setRepeat(repeat);
        Utils.saveRepeat(getActivity(), repeat);
    }

    @Override
    public void onResume() {
        super.onResume();
        isViewOn = true;
        seekBarHandleExecute();
        setPlayingInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        isViewOn = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.random_button:
                onRandomPressed();
                break;
            case R.id.repeat_button:
                onRepeatPressed();
                break;
            case R.id.play_button:
                onPlayPressed();
                break;
            case R.id.pause_button:
                onPausePressed();
                break;
            case R.id.stop_button:
                onStopPressed();
                break;
            case R.id.previous_button:
                onPreviousPressed();
                break;
            case R.id.next_button:
                onNextPressed();
                break;
        }
    }

    private void onRandomPressed() {
        boolean random = Utils.getRandom(getActivity());
        setRandom(!random);
    }

    private void onRepeatPressed() {
        Repeat repeat = Utils.getRepeat(getActivity());
        switch (repeat) {
            case REPEAT_OFF:
                repeat = Repeat.REPEAT_ALL;
                break;
            case REPEAT_ALL:
                repeat = Repeat.REPEAT_SINGLE;
                break;
            case REPEAT_SINGLE:
                repeat = Repeat.REPEAT_OFF;
                break;
        }
        setRepeat(repeat);
    }

    private void onPlayPressed() {
        if (!mediaPlayerService.isPlaying()) {
            mediaPlayerService.startTrack();
            seekBarHandleExecute();
        }
    }

    private void onPausePressed() {
        if (mediaPlayerService.isPlaying()) {
            mediaPlayerService.pauseTrack();
        }
    }

    private void onStopPressed() {
        mediaPlayerService.stopTrack();

        durationProgress.setProgress(0);
        playingDuration.setText(getString(R.string.track_duration_zero));
    }

    private void onPreviousPressed() {
        initTrack(mediaPlayerService.previousTrack());
    }

    private void onNextPressed() {
        initTrack(mediaPlayerService.nextTrack());
    }

    public void initTrack(Track track) {
        if (track != null) {
            showPlayingTrackInfo(track);
            durationProgress.setProgress(0);
            seekBarHandleExecute();

            ((MediaPlayerActivity) getActivity()).notifyCurrentList();
        }
    }

    public void initPlaylist(Playlist playlist, Track track) {
        mediaPlayerService.setPlaylist(playlist);
        mediaPlayerService.startTrack(track);
        initTrack(track);
    }

    public void showPlayingTrackInfo(Track track) {
        if (track != null) {
            playingTitle.setText(track.getTitle());
            playingArtist.setText(track.getArtist());
            playingDuration.setText(Utils.convertDuration(mediaPlayerService.getCurrentPosition()));
            maxDuration.setText(Utils.convertDuration(track.getDuration()));
            durationProgress.setMax(track.getDuration());
        } else {
            playingTitle.setText("");
            playingArtist.setText("");
            playingDuration.setText(getString(R.string.track_duration_zero));
            maxDuration.setText(getString(R.string.track_duration_zero));
            durationProgress.setProgress(0);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int value = durationProgress.getProgress();
        if (fromUser) {
            mediaPlayerService.setTrackPosition(value);
        }
        playingDuration.setText(Utils.convertDuration(value));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void trackCompleted() {
        initTrack(mediaPlayerService.getCurrentTrack());
    }

    public class SeekBarHandler extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            durationProgress.setProgress(mediaPlayerService.getCurrentPosition());
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            while (mediaPlayerService.isPlaying() && isViewOn) {
                if (isCancelled()) {
                    return null;
                }
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {

                }

                onProgressUpdate();
            }
            return null;
        }
    }

    public void seekBarHandleExecute() {
        if (seekBarHandler != null) {
            seekBarHandler.cancel(true);
        }
        seekBarHandler = new SeekBarHandler();
        seekBarHandler.execute();
    }

    public SeekBar getDurationProgress() {
        return durationProgress;
    }
}
