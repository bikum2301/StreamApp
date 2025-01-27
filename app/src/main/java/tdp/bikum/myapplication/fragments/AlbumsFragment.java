package tdp.bikum.myapplication.fragments;

import android.os.Bundle;
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
import tdp.bikum.myapplication.adapters.AlbumAdapter;
import tdp.bikum.myapplication.api.ApiService;
import tdp.bikum.myapplication.api.RetrofitClient;
import tdp.bikum.myapplication.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private List<Album> albumList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAlbums);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy danh sách album
        loadAlbums();

        // Thiết lập Adapter
        albumAdapter = new AlbumAdapter(getContext(), albumList);
        recyclerView.setAdapter(albumAdapter);

        return view;
    }

    private void loadAlbums() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Album>> call = apiService.getAlbums();
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    albumList.clear();
                    albumList.addAll(response.body());
                    albumAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }
}