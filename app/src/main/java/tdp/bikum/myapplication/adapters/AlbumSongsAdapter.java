package tdp.bikum.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.models.Song;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.ViewHolder> {
    private Context context;
    private List<Song> songs;

    public AlbumSongsAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.textViewTitle.setText(song.getTitle());
        // Convert duration from milliseconds to mm:ss format
        String duration = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(song.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(song.getDuration()) % 60
        );
        holder.textViewDuration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDuration;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
        }
    }
}