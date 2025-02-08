package tdp.bikum.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.activities.AlbumDetailActivity;
import tdp.bikum.myapplication.models.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albumList;

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.textViewName.setText(album.getName());
        holder.textViewSongCount.setText(album.getSongs().size() + " songs");

        // Load album cover if available
        if (album.getCoverUrl() != null) {
            Glide.with(context)
                    .load(album.getCoverUrl())
                    .placeholder(R.drawable.default_album_art)
                    .into(holder.imageViewCover);
        }

        // Handle click on album item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AlbumDetailActivity.class);
            intent.putExtra("album", album);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void updateAlbums(List<Album> newAlbums) {
        this.albumList = newAlbums;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewSongCount;
        ImageView imageViewCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSongCount = itemView.findViewById(R.id.textViewSongCount);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
        }
    }
}