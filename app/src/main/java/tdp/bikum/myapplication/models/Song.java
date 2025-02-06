package tdp.bikum.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Song implements Parcelable {
    private String title;
    private String artist;
    private String path;
    private long duration;

    // Constructor
    public Song(String title, String artist, String path, long duration) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }

    // Parcelable implementation
    protected Song(Parcel in) {
        title = in.readString();
        artist = in.readString();
        path = in.readString();
        duration = in.readLong();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(path);
        dest.writeLong(duration);
    }

    // Getters
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getPath() { return path; }
    public long getDuration() { return duration; }
}