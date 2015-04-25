package by.itrex.jetfirer.mediaplayer.model;

import java.util.List;

/**
 * Created by Konstantin on 25.04.2015.
 */
public class Playlist implements Comparable<Playlist> {

    private long id;
    private String name;
    private List<Track> tracks;

    public Playlist() {

    }

    public Playlist(long id, String name, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.tracks = tracks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tracks=" + tracks +
                '}';
    }

    @Override
    public int compareTo(Playlist playlist) {
        if (playlist == null || playlist.getName() == null || name == null) {
            return 0;
        }
        return name.compareTo(playlist.getName());
    }
}
