package tdp.bikum.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.api.ApiService;
import tdp.bikum.myapplication.api.RetrofitClient;
import tdp.bikum.myapplication.models.Lyrics;

public class LyricsFragment extends Fragment {
    private TextView lyricsTextView;
    private String songId;

    public static LyricsFragment newInstance(String songId) {
        LyricsFragment fragment = new LyricsFragment();
        Bundle args = new Bundle();
        args.putString("song_id", songId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);
        lyricsTextView = view.findViewById(R.id.lyricsTextView);

        if (getArguments() != null) {
            songId = getArguments().getString("song_id");
            loadLyrics(songId);
        }

        return view;
    }

    private void loadLyrics(String songId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Lyrics> call = apiService.getLyrics(songId);
        call.enqueue(new Callback<Lyrics>() {
            @Override
            public void onResponse(Call<Lyrics> call, Response<Lyrics> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lyricsTextView.setText(response.body().getText());
                }
            }

            @Override
            public void onFailure(Call<Lyrics> call, Throwable t) {
                lyricsTextView.setText(R.string.lyrics_load_error);
            }
        });
    }
}