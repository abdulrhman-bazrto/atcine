package com.gnusl.actine.ui.custom;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.ui.adapter.GenresAdapter;

import java.util.ArrayList;
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

        List<String> genres = new ArrayList<>();
        genres.add("Action");
        genres.add("Anime");
        genres.add("Award-winning");
        genres.add("Children & family");
        genres.add("Classic");
        genres.add("Comedies");
        genres.add("Crime");
        genres.add("Documentaries");
        genres.add("Drama");
        genres.add("Horror");
        genres.add("Independent");
        genres.add("Music & Musical");
        genres.add("Romance");
        genres.add("Sci-Fi & Fantasy");
        genres.add("Sports");
        genres.add("Stand Up Comedy");
        genres.add("Thrillers");
        genres.add("Audi Description");

        genresAdapter = new GenresAdapter(mContext, genres, genresClickEvents);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);

        rvGenres.setLayoutManager(layoutManager);

        rvGenres.setAdapter(genresAdapter);

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
        this.genresAdapter.setClickListener(genresClickEvents);
    }


}
