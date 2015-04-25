package by.itrex.jetfirer.mediaplayer.widget;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;
import by.itrex.jetfirer.mediaplayer.service.TrackCompletedListener;

/**
 * Created by Konstantin on 24.04.2015.
 */
public class MediaPlayerController extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, TrackCompletedListener {

    private MediaPlayerService mediaPlayerService;

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

        initMediaPlayerService();

        return view;
    }

    private void initMediaPlayerService() {
        mediaPlayerService = MediaPlayerService.getInstance();

        if (!mediaPlayerService.isPlaying()) {
            getActivity().startService(new Intent(getActivity(), MediaPlayerService.class));
        }
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
            case R.id.previous_button:
                onPreviousPressed();
                break;
            case R.id.stop_button:
                onStopPressed();
                break;
            case R.id.play_button:
                onPlayPressed();
                break;
            case R.id.pause_button:
                onPausePressed();
                break;
            case R.id.next_button:
                onNextPressed();
                break;
        }
    }

    private void onRandomPressed() {

    }

    private void onRepeatPressed() {

    }

    private void onPreviousPressed() {

    }

    private void onStopPressed() {

    }

    private void onPlayPressed() {

    }

    private void onPausePressed() {

    }

    private void onNextPressed() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int value = durationProgress.getProgress();
        if (fromUser) {
            mediaPlayerService.setTrackPosition(value);
        }
        playingDuration.setText(convertDuration(value));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void trackCompleted() {

    }

    private String convertDuration(int value) {
        value /= 1000;
        int minutes = value / 60;
        int seconds = value - minutes * 60;

        StringBuilder durationBuilder = new StringBuilder();
        if (minutes < 10) {
            durationBuilder.append("0");
        }
        durationBuilder.append(minutes).append(":");
        if (seconds < 10) {
            durationBuilder.append("0");
        }
        durationBuilder.append(seconds);
        return durationBuilder.toString();
    }
}
