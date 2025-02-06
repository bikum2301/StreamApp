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
    private SeekBar seekBar;
    private ImageButton playPauseButton, rewindButton, nextButton;
    private List<Song> songList;
    private int currentPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songList = getArguments().getParcelableArrayList("songList");
            String currentSongPath = getArguments().getString("currentSongPath");

            // Find current song position
            if (songList != null) {
                for (int i = 0; i < songList.size(); i++) {
                    if (songList.get(i).getPath().equals(currentSongPath)) {
                        currentPosition = i;
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        songTitleTextView = view.findViewById(R.id.songTitleTextView);
        songArtistTextView = view.findViewById(R.id.songArtistTextView);
        seekBar = view.findViewById(R.id.seekBar);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        rewindButton = view.findViewById(R.id.rewindButton);
        nextButton = view.findViewById(R.id.nextButton);

        setupPlayer();
        setupListeners();

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

        rewindButton.setOnClickListener(v -> player.seekTo(player.getCurrentPosition() - 10000));

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

        songTitleTextView.setText(song.getTitle());
        songArtistTextView.setText(song.getArtist());
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void playNextSong() {
        if (songList != null && !songList.isEmpty()) {
            currentPosition = (currentPosition + 1) % songList.size();
            playSong(songList.get(currentPosition));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}