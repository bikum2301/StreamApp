package tdp.bikum.myapplication.models;

public class Lyrics {
    private String songId;
    private String text;
    private String language;

    public Lyrics(String songId, String text, String language) {
        this.songId = songId;
        this.text = text;
        this.language = language;
    }

    public String getSongId() { return songId; }
    public String getText() { return text; }
    public String getLanguage() { return language; }
}
