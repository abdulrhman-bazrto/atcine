package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.model.DBShow;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.VideoActivity;
import com.gnusl.actine.util.ObjectBox;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class DownloadsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Show> shows = new ArrayList<>();


    public DownloadsListAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_download, parent, false);
        return new MovieListViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((MovieListViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public void setList(List<Show> movies) {
        this.shows = movies;
        notifyDataSetChanged();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivShowImage,ivPlay;
        private TextView tvShowName, tvShowSize, tvImdb, tvTomato;
        private ImageButton btnDelete, btnDownload;
        private View iv_tomato;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivShowImage = itemView.findViewById(R.id.iv_show_image);
            tvShowName = itemView.findViewById(R.id.tv_show_name);
            tvShowSize = itemView.findViewById(R.id.tv_file_size);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDownload = itemView.findViewById(R.id.btn_download);
            tvImdb = itemView.findViewById(R.id.tv_imdb_rate);
            tvTomato = itemView.findViewById(R.id.tv_tomato_rate);
            iv_tomato = itemView.findViewById(R.id.iv_tomato);
            ivPlay = itemView.findViewById(R.id.iv_play);
        }

        public void bind() {
            final Show show = shows.get(getAdapterPosition());

            Picasso.with(mContext).load(show.getThumbnailImageUrl()).into(ivShowImage);

            tvShowName.setText(show.getTitle());

            tvShowSize.setText(show.getSize());
            tvImdb.setText(show.getImdbRate().toString());
            if (!show.getRottenTomatoes().isEmpty()) {
                tvTomato.setText(show.getRottenTomatoes());
            }else {
                tvTomato.setVisibility(View.GONE);
                iv_tomato.setVisibility(View.GONE);
            }
            if (show.isInStorage()) {
                btnDelete.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
            }

            ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playMovie(show);
                }
            });
            ivShowImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playMovie(show);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File internalStorage = mContext.getFilesDir();
                    File file = new File(internalStorage, show.getTitle() + ".mp4");
                    file.delete();
                    btnDelete.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.VISIBLE);
                    show.setInStorage(false);
                    DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())), null);

                    Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
                    DBShow dbShowInBox = dbShowBox.get(show.getId());

                    if (dbShowInBox != null) {
                        dbShowBox.remove(show.getId());
                    }
                }
            });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, R.string.downloading1, Toast.LENGTH_SHORT).show();

                    File internalStorage = mContext.getFilesDir();
//                    String url = show.getVideoUrl();
//                    if (url.contains("_.m3u8")) {
//                        url = url.replaceAll("_.m3u8", ".mp4");
//                    } else {
//                        url = url.replaceAll(".m3u8", ".mp4");
//                    }

                    String url = show.getDownloadVideoUrl();
                    DataLoader.downloadRequest(mContext, show.getId(), url, internalStorage.getAbsolutePath(), show.getTitle() + ".mp4", new DownloadDelegate() {
                        @Override
                        public void onDownloadProgress(String fileDir, String fileName, int progress) {

                        }

                        @Override
                        public void onDownloadError(ANError anError) {
                            Log.d("", "");
                        }

                        @Override
                        public void onDownloadSuccess(String fileDir, String fileName) {
                            btnDelete.setVisibility(View.VISIBLE);
                            btnDownload.setVisibility(View.GONE);
                            show.setIsDownloaded(true);
                            show.setInStorage(true);
                            DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())), null);
                        }
                    });

                }
            });
        }
    }

    private void playMovie(Show show) {
        if (show.isInStorage()) {
            File internalStorage = mContext.getFilesDir();
            File file = new File(internalStorage, show.getTitle() + ".mp4");
            Intent intent = new Intent(mContext, VideoActivity.class);
            intent.putExtra("url", file.getAbsolutePath());
            mContext.startActivity(intent);

//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file.getAbsolutePath()));
//                        intent.setDataAndType(Uri.parse(file.getAbsolutePath()), "video/mp4");
//                        mContext.startActivity(intent);

        } else {
            Toast.makeText(mContext, R.string.download_first, Toast.LENGTH_LONG).show();
        }
    }
}
