package tdp.bikum.myapplication.models;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String path;

    // Constructor, getters, and setters
    public Song(String id, String title, String artist, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }
}