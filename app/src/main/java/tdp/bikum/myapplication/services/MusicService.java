package tdp.bikum.myapplication.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.activities.MainActivity;
import tdp.bikum.myapplication.models.Song;

public class MusicService extends Service {
    private ExoPlayer player;
    private final IBinder binder = new LocalBinder();
    private static final String CHANNEL_ID = "music_channel";
    private static final int NOTIFICATION_ID = 1;
    private Song currentSong;

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializePlayer();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            handleActionIntent(intent.getAction());
        }

        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    private void handleActionIntent(String action) {
        switch (action) {
            case "PREVIOUS":
                // Handle previous
                break;
            case "PLAY_PAUSE":
                if (player.isPlaying()) {
                    player.pause();
                } else {
                    player.play();
                }
                break;
            case "NEXT":
                // Handle next
                break;
        }
    }

    private void initializePlayer() {
        if (player == null) {
            player = new ExoPlayer.Builder(this).build();
            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    updateNotification();
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    updateNotification();
                }
            });
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, MusicService.class).setAction("PREVIOUS");
        PendingIntent prevPendingIntent = PendingIntent.getService(this, 0, prevIntent,
                PendingIntent.FLAG_IMMUTABLE);

        Intent playIntent = new Intent(this, MusicService.class).setAction("PLAY_PAUSE");
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent,
                PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, MusicService.class).setAction("NEXT");
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 0, nextIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle(currentSong != null ? currentSong.getTitle() : "Đang phát nhạc")
                .setContentText(currentSong != null ? currentSong.getArtist() : "")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_skip_previous, "Previous", prevPendingIntent)
                .addAction(player != null && player.isPlaying() ?
                                R.drawable.ic_pause : R.drawable.ic_play,
                        "Play/Pause", playPendingIntent)
                .addAction(R.drawable.ic_skip_next, "Next", nextPendingIntent)
                .build();
    }

    private void updateNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, createNotification());
    }

    public ExoPlayer getPlayer() {
        if (player == null) {
            initializePlayer();
        }
        return player;
    }

    public void playSong(Song song) {
        currentSong = song;
        if (player != null) {
            player.setMediaItem(MediaItem.fromUri(Uri.parse(song.getPath())));
            player.prepare();
            player.play();
            updateNotification();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}