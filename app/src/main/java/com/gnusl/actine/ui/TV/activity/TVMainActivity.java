package com.gnusl.actine.ui.TV.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gnusl.actine.R;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.model.TabObject;
import com.gnusl.actine.ui.Mobile.adapter.GenresAdapter;
import com.gnusl.actine.ui.Mobile.adapter.MainFragmentPagerAdapter;
import com.gnusl.actine.ui.Mobile.adapter.TabsAdapter;
import com.gnusl.actine.ui.Mobile.fragment.HomeFragment;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Locale;

public class TVMainActivity extends AppCompatActivity {
    private RecyclerView rvTabs;
    private TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);

        String languageToLoad = SharedPreferencesUtils.getLanguage(Atcine.getApplicationInstance());
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_tvmain);
        init();

    }

    private void init() {

        findViews();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        ArrayList<TabObject> tabs = new ArrayList<>();
        tabs.add(new TabObject("Home",R.drawable.icon_account));
        tabs.add(new TabObject("Movies",R.drawable.icon_account));
        tabs.add(new TabObject("Series",R.drawable.icon_account));
        tabs.add(new TabObject("MyList",R.drawable.icon_account));
        tabsAdapter = new TabsAdapter(TVMainActivity.this, tabs);

        rvTabs.setLayoutManager(gridLayoutManager);
        rvTabs.setAdapter(tabsAdapter);

    }

    private void findViews() {
        rvTabs = findViewById(R.id.rv_tabs);
    }

}
