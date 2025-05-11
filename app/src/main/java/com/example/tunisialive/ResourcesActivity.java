package com.example.tunisialive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResourcesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResourcesAdapter adapter;
    private List<Resource> resourcesList;
    private SharedPreferences sharedPreferences;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        // Set up toolbar with back button
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        preferencesManager = new PreferencesManager(this);
        sharedPreferences = getSharedPreferences("RSS_PREFS", MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerViewResources);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resourcesList = loadResources();
        adapter = new ResourcesAdapter(this, resourcesList, preferencesManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // go back when back arrow is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Resource> loadResources() {
        List<Resource> list = new ArrayList<>();
        Set<String> selectedSources = sharedPreferences.getStringSet("SELECTED_RSS", new HashSet<>());

        list.add(new Resource("Kapitalis", "https://kapitalis.com/tunisie/feed/", selectedSources.contains("https://kapitalis.com/tunisie/feed/")));
        list.add(new Resource("Mosaïque FM", "https://www.mosaiquefm.net/ar/rss", selectedSources.contains("https://www.mosaiquefm.net/ar/rss")));
        list.add(new Resource("La Presse", "https://lapresse.tn/feed/", selectedSources.contains("https://lapresse.tn/feed/")));
        list.add(new Resource("Alchourouk", "https://www.alchourouk.com/rss", selectedSources.contains("https://www.alchourouk.com/rss")));
        list.add(new Resource("Brands", "https://brands.com.tn/feed/", selectedSources.contains("https://brands.com.tn/feed/")));
        list.add(new Resource("Africanmanager", "https://ar.africanmanager.com/feed/", selectedSources.contains("https://ar.africanmanager.com/feed/")));
        list.add(new Resource("Tuniscope", "https://www.tuniscope.com/feed", selectedSources.contains("https://www.tuniscope.com/feed")));
        list.add(new Resource("Leaders", "https://ar.leaders.com.tn/rss", selectedSources.contains("https://ar.leaders.com.tn/rss")));
        list.add(new Resource("Hakaek Online", "https://hakaekonline.com/feed/", selectedSources.contains("https://hakaekonline.com/feed/")));
        list.add(new Resource("Business News", "https://www.businessnews.com.tn/rss.xml", selectedSources.contains("https://www.businessnews.com.tn/rss.xml")));
        list.add(new Resource("Babnet", "https://www.babnet.net/feed.php", selectedSources.contains("https://www.babnet.net/feed.php")));
        list.add(new Resource("Arrakmia", "https://www.arrakmia.com/feed-actualites-tunisie.xml", selectedSources.contains("https://www.arrakmia.com/feed-actualites-tunisie.xml")));
        list.add(new Resource("Arabesque", "https://www.arabesque.tn/ar/rss", selectedSources.contains("https://www.arabesque.tn/ar/rss")));
        list.add(new Resource("Radio Express FM", "https://radioexpressfm.com/ar/feed/", selectedSources.contains("https://radioexpressfm.com/ar/feed/")));
        list.add(new Resource("Webmanagercenter", "http://ar.webmanagercenter.com/feed/", selectedSources.contains("http://ar.webmanagercenter.com/feed/")));
        list.add(new Resource("Rassd Tunisia", "https://rassdtunisia.net/feed/", selectedSources.contains("https://rassdtunisia.net/feed/")));
        list.add(new Resource("Jawhara FM", "https://www.jawharafm.net/ar/rss/showRss/88/1/1", selectedSources.contains("https://www.jawharafm.net/ar/rss/showRss/88/1/1")));
        return list;
    }
}
