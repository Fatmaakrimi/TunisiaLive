package com.example.tunisialive;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResourceArticlesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ApiService apiService;
    private String resourceName;
    private String resourceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_articles);

        // Setup the Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // optional: if you're using menu actions
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(""); // clear title, use custom one if needed
        }
        toolbar.setTitle("Actualités"); // Or use resourceName if you prefer dynamic title
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewArticles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get resource info from Intent
        resourceName = getIntent().getStringExtra("RESOURCE_NAME");
        resourceUrl = getIntent().getStringExtra("RESOURCE_URL");

        if (resourceUrl != null) {
            fetchArticles(resourceUrl);
        } else {
            Toast.makeText(this, "Invalid resource", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchArticles(String url) {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getNews(url).enqueue(new Callback<RSSFeed>() {
            @Override
            public void onResponse(Call<RSSFeed> call, Response<RSSFeed> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsItem> newsList = response.body().getChannel().getNewsItems();
                    Collections.sort(newsList, (a, b) -> b.getPubDate().compareTo(a.getPubDate()));
                    adapter = new NewsAdapter(ResourceArticlesActivity.this, newsList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ResourceArticlesActivity.this, "No articles found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RSSFeed> call, Throwable t) {
                Log.e("RESOURCE_ARTICLES", "Failed to fetch: " + t.getMessage());
                Toast.makeText(ResourceArticlesActivity.this, "Failed to load articles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
