package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.VideoActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

        private ImageView ivShowImage;
        private TextView tvShowName, tvShowSize;
        private Button btnDelete, btnDownload;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivShowImage = itemView.findViewById(R.id.iv_show_image);
            tvShowName = itemView.findViewById(R.id.tv_show_name);
            tvShowSize = itemView.findViewById(R.id.tv_file_size);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDownload = itemView.findViewById(R.id.btn_download);
        }

        public void bind() {
            final Show show = shows.get(getAdapterPosition());

            Picasso.with(mContext).load(show.getThumbnailImageUrl()).into(ivShowImage);

            tvShowName.setText(show.getTitle());

            tvShowSize.setText(show.getSize());

            if (show.isInStorage()) {
                btnDelete.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show.isInStorage()) {
                        File internalStorage = mContext.getFilesDir();
                        File file = new File(internalStorage, show.getTitle() + ".mp4");
                        Intent intent = new Intent(mContext, VideoActivity.class);
                        intent.putExtra("url", file.getAbsolutePath());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "download first", Toast.LENGTH_LONG).show();
                    }
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
//                    DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())),this);
                }
            });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"Downloading",Toast.LENGTH_SHORT).show();

                    File internalStorage = mContext.getFilesDir();
                    String url = show.getVideoUrl();
                    if (url.contains("_.m3u8")) {
                        url = url.replaceAll("_.m3u8", ".mp4");
                    } else {
                        url = url.replaceAll(".m3u8", ".mp4");
                    }
                    DataLoader.downloadRequest(url, internalStorage.getAbsolutePath(), show.getTitle() + ".mp4", new DownloadDelegate() {
                        @Override
                        public void onDownloadProgress(String fileDir, String fileName, int progress) {

                        }

                        @Override
                        public void onDownloadError(ANError anError) {

                        }

                        @Override
                        public void onDownloadSuccess(String fileDir, String fileName) {
                            btnDelete.setVisibility(View.VISIBLE);
                            btnDownload.setVisibility(View.GONE);
                            show.setInStorage(true);
//                            DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())),null);
                        }
                    });

                }
            });
        }
    }
}
