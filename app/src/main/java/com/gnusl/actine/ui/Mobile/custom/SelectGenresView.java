package com.gnusl.actine.ui.Mobile.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.ui.Mobile.adapter.GenresAdapter;

import java.util.List;


public class SelectGenresView extends ConstraintLayout {

    ImageView ivClose;
    RecyclerView rvGenres;
    Context mContext;
    private GenresClickEvents genresClickEvents;
    private GenresAdapter genresAdapter;

    public SelectGenresView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public SelectGenresView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public SelectGenresView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_select_genres, SelectGenresView.this);

        ivClose = findViewById(R.id.iv_close);
        rvGenres = findViewById(R.id.rv_genres);

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genresClickEvents != null)
                    genresClickEvents.onCloseGenres();
            }
        });

    }

    public void setClickListener(GenresClickEvents genresClickEvents) {
        this.genresClickEvents = genresClickEvents;
    }


    public void setList(List<Category> categories) {

        genresAdapter = new GenresAdapter(mContext,categories , genresClickEvents);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);

        rvGenres.setLayoutManager(layoutManager);

        rvGenres.setAdapter(genresAdapter);

        this.genresAdapter.setClickListener(genresClickEvents);

        if (genresAdapter != null)
            genresAdapter.setList(categories);
    }
}
