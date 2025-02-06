package tdp.bikum.myapplication.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.adapters.SongAdapter;
import tdp.bikum.myapplication.api.ApiService;
import tdp.bikum.myapplication.api.RetrofitClient;
import tdp.bikum.myapplication.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList = new ArrayList<>();
    private PlayerFragment playerFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        songAdapter = new SongAdapter(getContext(), songList);
        songAdapter.setOnSongClickListener(song -> {
            PlayerFragment playerFragment = new PlayerFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList("songList", new ArrayList<Parcelable>(songList));
            args.putString("currentSongPath", song.getPath());
            playerFragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, playerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(songAdapter);
        loadSongsFromDevice();

        return view;
    }

    private void loadSongsFromDevice() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, null, sortOrder);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                if (titleIndex != -1 && artistIndex != -1 && pathIndex != -1 && durationIndex != -1) {
                    String title = cursor.getString(titleIndex);
                    String artist = cursor.getString(artistIndex);
                    String path = cursor.getString(pathIndex);
                    long duration = cursor.getLong(durationIndex);

                    Song song = new Song(title, artist, path, duration);
                    songList.add(song);
                }
            }
            cursor.close();
        }

        songAdapter.notifyDataSetChanged();
    }
}