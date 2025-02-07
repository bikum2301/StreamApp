package tdp.bikum.myapplication.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

import java.util.List;
import java.util.Locale;

import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.interfaces.OnSongClickListener;
import tdp.bikum.myapplication.models.Song;
import tdp.bikum.myapplication.services.MusicService;

public class PlayerFragment extends Fragment implements OnSongClickListener {
    private ExoPlayer player;
    private TextView songTitleTextView, songArtistTextView;
    private TextView currentTimeTextView, totalTimeTextView;
    private SeekBar seekBar;
    private ImageButton playPauseButton, previousButton, nextButton;
    private List<Song> songList;
    private int currentPosition = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isDragging = false;
    private MusicService musicService;
    private boolean isBound = false;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            player = musicService.getPlayer();
            isBound = true;
            if (player != null) {
                setupPlayer();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            player = null;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // Bind to MusicService
        Intent intent = new Intent(getContext(), MusicService.class);
        requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initializeViews(view);
        setupListeners();
        return view;
    }

    private void initializeViews(View view) {
        songTitleTextView = view.findViewById(R.id.songTitleTextView);
        songArtistTextView = view.findViewById(R.id.songArtistTextView);
        currentTimeTextView = view.findViewById(R.id.currentTimeTextView);
        totalTimeTextView = view.findViewById(R.id.totalTimeTextView);
        seekBar = view.findViewById(R.id.seekBar);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextButton);
    }

    private void setupPlayer() {
        if (player == null) return;

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    seekBar.setMax((int) player.getDuration());
                    totalTimeTextView.setText(formatTime(player.getDuration()));
                    startProgressUpdate();
                } else if (playbackState == Player.STATE_ENDED) {
                    playNextSong();
                }
            }
        });
    }

    private void setupListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && player != null) {
                    player.seekTo(progress);
                    currentTimeTextView.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDragging = true;
                handler.removeCallbacks(updateProgressRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isDragging = false;
                if (player != null) {
                    player.seekTo(seekBar.getProgress());
                    startProgressUpdate();
                }
            }
        });

        playPauseButton.setOnClickListener(v -> togglePlayPause());
        previousButton.setOnClickListener(v -> playPreviousSong());
        nextButton.setOnClickListener(v -> playNextSong());
    }

    private final Runnable updateProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (player != null && !isDragging && player.isPlaying()) {
                long currentPosition = player.getCurrentPosition();
                seekBar.setProgress((int) currentPosition);
                currentTimeTextView.setText(formatTime(currentPosition));
                handler.postDelayed(this, 500);
            }
        }
    };

    private void startProgressUpdate() {
        handler.removeCallbacks(updateProgressRunnable);
        handler.post(updateProgressRunnable);
    }

    private void togglePlayPause() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
                playPauseButton.setImageResource(R.drawable.ic_play);
            } else {
                player.play();
                playPauseButton.setImageResource(R.drawable.ic_pause);
                startProgressUpdate();
            }
        }
    }

    @Override
    public void onSongClick(Song song) {
        if (musicService != null) {
            musicService.playSong(song);
            updateUI(song);
        }
    }

    private void updateUI(Song song) {
        songTitleTextView.setText(song.getTitle());
        songArtistTextView.setText(song.getArtist());
        playPauseButton.setImageResource(R.drawable.ic_pause);
    }

    private void playNextSong() {
        if (songList != null && !songList.isEmpty()) {
            currentPosition = (currentPosition + 1) % songList.size();
            Song nextSong = songList.get(currentPosition);
            if (musicService != null) {
                musicService.playSong(nextSong);
                updateUI(nextSong);
            }
        }
    }

    private void playPreviousSong() {
        if (songList != null && !songList.isEmpty()) {
            currentPosition = (currentPosition - 1 + songList.size()) % songList.size();
            Song prevSong = songList.get(currentPosition);
            if (musicService != null) {
                musicService.playSong(prevSong);
                updateUI(prevSong);
            }
        }
    }

    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateProgressRunnable);
        if (isBound) {
            requireActivity().unbindService(serviceConnection);
            isBound = false;
        }
    }
}