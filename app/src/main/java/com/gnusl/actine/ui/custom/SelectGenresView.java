package com.gnusl.actine.ui.custom;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.model.Category;
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

        List<Category> genres = new ArrayList<>();
        genres.add(new Category(1, "Action"));
        genres.add(new Category(2, "Anime"));
        genres.add(new Category(3, "Award-winning"));
        genres.add(new Category(4, "Children & family"));
        genres.add(new Category(5, "Classic"));
        genres.add(new Category(6, "Comedies"));
        genres.add(new Category(7, "Crime"));
        genres.add(new Category(8, "Documentaries"));
        genres.add(new Category(9, "Drama"));
        genres.add(new Category(10, "Horror"));
        genres.add(new Category(11, "Independent"));
        genres.add(new Category(12, "Music & Musical"));
        genres.add(new Category(13, "Romance"));
        genres.add(new Category(14, "Sci-Fi & Fantasy"));
        genres.add(new Category(15, "Sports"));
        genres.add(new Category(16, "Stand Up Comedy"));
        genres.add(new Category(17, "Thrillers"));
        genres.add(new Category(18, "Audi Description"));

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


    public void setList(List<Category> categories) {
        if (genresAdapter != null)
            genresAdapter.setList(categories);
    }
}
