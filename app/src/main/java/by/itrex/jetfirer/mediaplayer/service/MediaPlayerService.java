package by.itrex.jetfirer.mediaplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.List;
import java.util.Random;
import java.util.Stack;

import by.itrex.jetfirer.mediaplayer.enums.Repeat;
import by.itrex.jetfirer.mediaplayer.model.Playlist;
import by.itrex.jetfirer.mediaplayer.model.Track;

/**
 * Created by Konstantin on 25.04.2015.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener{

    private static MediaPlayer mediaPlayer = new MediaPlayer();

    private Repeat repeat = Repeat.REPEAT_OFF;
    private boolean random = false;

    private Track currentTrack;
    private Playlist playlist;
    private Stack<Track> previousTracks;
    private Stack<Track> nextTracks;

    private TrackCompletedListener trackComplitedListener;

    private static MediaPlayerService mediaPlayerService = new MediaPlayerService();

    public static MediaPlayerService getInstance() {
        return mediaPlayerService;
    }

    private MediaPlayerService() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        previousTracks = new Stack<>();
        nextTracks = new Stack<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    private void setTrack(Track track) {
        try {
            if (track != null) {
                currentTrack = track;

                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, track.getData());
                } else {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(this, track.getData());
                    mediaPlayer.prepare();
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void startTrack() {
        if (mediaPlayer != null && currentTrack != null) {
            setTrack(currentTrack);
            mediaPlayer.start();
        }
    }

    public void startTrack(Track track) {
        setTrack(track);
        startTrack();
    }

    public void stopTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            setTrack(currentTrack);
        }
    }

    public void pauseTrack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public Track previousTrack() {
        Track track = getPreviousTrack();
        if (track != null) {
            if (isPlaying()) {
                startTrack(track);
            } else {
                setTrack(track);
            }
        }

        return track;
    }

    public Track nextTrack() {
        Track track = getNextTrack();
        if (track != null) {
            if (isPlaying()) {
                startTrack(track);
            } else {
                setTrack(track);
            }
        }

        return track;
    }

    public void setTrackPosition(int position) {
        if (mediaPlayer != null && currentTrack != null && position >= 0 && position <= currentTrack.getDuration()) {
            mediaPlayer.seekTo(position);
        }
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    private Track getPreviousTrack() {
        if (playlist != null && currentTrack != null && playlist.getTracks() != null) {
            if (repeat == Repeat.REPEAT_SINGLE) {
                return currentTrack;
            }

            if (random) {
                if (!previousTracks.isEmpty()) {
                    return previousTracks.pop();
                } else {
                    return getRandomTrack();
                }
            } else {
                List<Track> tracks = playlist.getTracks();
                for (int i = 0; i < tracks.size(); i++) {
                    if (tracks.get(i).equals(currentTrack)) {
                        if (i > 0) {
                            return tracks.get(i - 1);
                        } else {
                            if (repeat == Repeat.REPEAT_ALL) {
                                return tracks.get(tracks.size() - 1);
                            }
                        }
                        break;
                    }
                }
            }
        }

        return null;
    }

    private Track getNextTrack() {
        if (playlist!= null && currentTrack != null && playlist.getTracks() != null) {
            if (repeat == Repeat.REPEAT_SINGLE) {
                return currentTrack;
            }

            if (random) {
                if (!nextTracks.isEmpty()) {
                    return nextTracks.pop();
                } else {
                    return getRandomTrack();
                }
            } else {
                List<Track> tracks = playlist.getTracks();
                for (int i = 0; i < tracks.size(); i++) {
                    if (tracks.get(i).equals(currentTrack)) {
                        if (i < tracks.size() - 1) {
                            return tracks.get(i + 1);
                        } else {
                            if (repeat == Repeat.REPEAT_ALL) {
                                return tracks.get(0);
                            }
                        }
                        break;
                    }
                }
            }
        }

        return null;
    }

    private Track getRandomTrack() {
        Track track = null;
        if (playlist != null) {
            List<Track> tracks = playlist.getTracks();
            if (tracks != null && !tracks.isEmpty()) {
                if (tracks.size() == 1) {
                    return currentTrack;
                } else {
                    Random r = new Random();
                    do {
                        int position = r.nextInt(tracks.size() - 1);
                        track = tracks.get(position);
                    } while (track.equals(currentTrack));
                }
            }
        }

        return track;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Stack<Track> getPreviousTracks() {
        return previousTracks;
    }

    public void setPreviousTracks(Stack<Track> previousTracks) {
        this.previousTracks = previousTracks;
    }

    public Stack<Track> getNextTracks() {
        return nextTracks;
    }

    public void setNextTracks(Stack<Track> nextTracks) {
        this.nextTracks = nextTracks;
    }

    public void setTrackComplitedListener(TrackCompletedListener trackComplitedListener) {
        this.trackComplitedListener = trackComplitedListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextTrack();

        if (trackComplitedListener != null) {
            trackComplitedListener.trackCompleted();
        }
    }
}
