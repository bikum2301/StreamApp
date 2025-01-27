package tdp.bikum.myapplication.models;

import java.util.List;

public class Album {
    private String id;
    private String name;
    private List<Song> songs;

    // Constructor, getters, and setters
    public Album(String id, String name, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }
}