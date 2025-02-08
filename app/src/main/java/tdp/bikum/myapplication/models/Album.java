package tdp.bikum.myapplication.models;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    private String id;
    private String name;
    private String coverUrl;
    private List<Song> songs;

    public Album(String id, String name, String coverUrl, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.songs = songs;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCoverUrl() { return coverUrl; }
    public List<Song> getSongs() { return songs; }
}