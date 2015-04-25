package by.itrex.jetfirer.mediaplayer.model;

import android.net.Uri;

/**
 * Created by Konstantin on 25.04.2015.
 */
public class Track implements Comparable<Track> {

    private long id;
    private String title;
    private String artist;
    private String album;
    private Uri data;
    private int duration;

    public Track() {

    }

    public Track(long id, String title, String artist, String album, Uri data, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.data = data;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", data=" + data +
                ", duration=" + duration +
                '}';
    }


    @Override
    public int compareTo(Track track) {
        if (track == null || track.title == null || track.artist == null || title == null || artist == null) {
            return 0;
        }
        return title.compareTo(track.title) != 0 ? title.compareTo(track.title) : artist.compareTo(track.artist);
    }
}
