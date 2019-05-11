package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.adapter.DownloadsListAdapter;


public class DownloadFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private Button btnFindDownload;
    private View clEmptyDownloadList, clDownloadList;
    private RecyclerView rvDownloads;
    private DownloadsListAdapter downloadsListAdapter;


    public DownloadFragment() {
    }

    public static DownloadFragment newInstance() {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_download, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();
        btnFindDownload.setOnClickListener(this);

        downloadsListAdapter = new DownloadsListAdapter(getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        rvDownloads.setLayoutManager(gridLayoutManager);

        rvDownloads.setAdapter(downloadsListAdapter);

    }

    private void findViews() {
        rvDownloads = inflatedView.findViewById(R.id.rv_downloads);
        btnFindDownload = inflatedView.findViewById(R.id.btn_find_download);
        clEmptyDownloadList = inflatedView.findViewById(R.id.cl_empty_downloads_hint);
        clDownloadList = inflatedView.findViewById(R.id.cl_download_list);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_download: {
                clEmptyDownloadList.setVisibility(View.GONE);
                clDownloadList.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

}
