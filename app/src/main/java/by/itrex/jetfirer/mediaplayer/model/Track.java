package by.itrex.jetfirer.mediaplayer.model;

/**
 * Created by Konstantin on 25.04.2015.
 */
public class Track implements Comparable<Track> {

    private long id;
    private String title;
    private String artist;
    private String album;
    private String data;
    private int duration;

    public Track() {

    }

    public Track(long id, String title, String artist, String album, String data, int duration) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
                ", data='" + data + '\'' +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (duration != track.duration) return false;
        if (title != null ? !title.equals(track.title) : track.title != null) return false;
        if (artist != null ? !artist.equals(track.artist) : track.artist != null) return false;
        if (album != null ? !album.equals(track.album) : track.album != null) return false;
        return !(data != null ? !data.equals(track.data) : track.data != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + duration;
        return result;
    }

    @Override
    public int compareTo(Track track) {
        if (track == null || track.title == null || track.artist == null || title == null || artist == null) {
            return 0;
        }
        return title.compareTo(track.title) != 0 ? title.compareTo(track.title) : artist.compareTo(track.artist);
    }
}
