package tdp.bikum.myapplication.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.adapters.AlbumSongsAdapter;
import tdp.bikum.myapplication.models.Album;

public class AlbumDetailActivity extends AppCompatActivity {
    private TextView albumNameTv;
    private ImageView albumCoverIv;
    private RecyclerView songsRv;
    private AlbumSongsAdapter adapter;
    private FloatingActionButton editFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        // Initialize views
        albumNameTv = findViewById(R.id.albumNameTextView);
        albumCoverIv = findViewById(R.id.albumCoverImageView);
        songsRv = findViewById(R.id.albumSongsRecyclerView);
        editFab = findViewById(R.id.editAlbumFab);

        // Get album data from intent
        Album album = (Album) getIntent().getSerializableExtra("album");
        if (album != null) {
            setupAlbumDetails(album);
        }

        editFab.setOnClickListener(v -> {
            // Launch edit album dialog/activity
            showEditAlbumDialog(album);
        });
    }

    private void setupAlbumDetails(Album album) {
        albumNameTv.setText(album.getName());
        // Set up RecyclerView
        adapter = new AlbumSongsAdapter(this, album.getSongs());
        songsRv.setLayoutManager(new LinearLayoutManager(this));
        songsRv.setAdapter(adapter);
    }

    private void showEditAlbumDialog(Album album) {
        // Implement edit album dialog
    }
}