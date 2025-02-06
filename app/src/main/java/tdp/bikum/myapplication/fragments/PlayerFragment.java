package tdp.bikum.myapplication.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.interfaces.OnSongClickListener;
import tdp.bikum.myapplication.models.Song;

public class PlayerFragment extends Fragment implements OnSongClickListener {
    private SimpleExoPlayer player;
    private TextView songTitleTextView, songArtistTextView;
    private TextView currentTimeTextView, totalTimeTextView;
    private SeekBar seekBar;
    private ImageButton playPauseButton, previousButton, nextButton, shuffleButton, repeatButton;
    private List<Song> songList;
    private int currentPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        // Initialize TextViews
        songTitleTextView = view.findViewById(R.id.songTitleTextView);
        songArtistTextView = view.findViewById(R.id.songArtistTextView);
        currentTimeTextView = view.findViewById(R.id.currentTimeTextView);
        totalTimeTextView = view.findViewById(R.id.totalTimeTextView);

        // Initialize SeekBar
        seekBar = view.findViewById(R.id.seekBar);

        // Initialize Buttons
        playPauseButton = view.findViewById(R.id.playPauseButton);
        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextButton);
        shuffleButton = view.findViewById(R.id.shuffleButton);
        repeatButton = view.findViewById(R.id.repeatButton);

        setupPlayer();
        setupListeners();

        // Play the initial song if arguments are passed
        if (getArguments() != null) {
            songList = getArguments().getParcelableArrayList("songList");
            String currentSongPath = getArguments().getString("currentSongPath");

            // Find current song position
            if (songList != null) {
                for (int i = 0; i < songList.size(); i++) {
                    if (songList.get(i).getPath().equals(currentSongPath)) {
                        currentPosition = i;
                        playSong(songList.get(currentPosition));
                        break;
                    }
                }
            }
        }

        return view;
    }

    private void setupPlayer() {
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) player.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupListeners() {
        playPauseButton.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            } else {
                player.play();
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        previousButton.setOnClickListener(v -> playPreviousSong());
        nextButton.setOnClickListener(v -> playNextSong());
    }

    @Override
    public void onSongClick(Song song) {
        playSong(song);
    }

    private void playSong(Song song) {
        if (player != null) {
            player.release();
        }

        player = new SimpleExoPlayer.Builder(requireContext()).build();
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(song.getPath()));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        // Update UI
        songTitleTextView.setText(song.getTitle());
        songArtistTextView.setText(song.getArtist());

        // Update total time
        totalTimeTextView.setText(formatTime(song.getDuration()));

        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void playNextSong() {
        if (songList != null && !songList.isEmpty()) {
            currentPosition = (currentPosition + 1) % songList.size();
            playSong(songList.get(currentPosition));
        }
    }

    private void playPreviousSong() {
        if (songList != null && !songList.isEmpty()) {
            currentPosition = (currentPosition - 1 + songList.size()) % songList.size();
            playSong(songList.get(currentPosition));
        }
    }

    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}