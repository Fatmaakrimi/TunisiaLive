package com.example.tunisialive;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryNewsActivity extends AppCompatActivity {

    private static final String TAG = "CategoryNewsActivity";

    private String categoryUrl;
    private String categoryTitle;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news);

        TextView categoryTitleTextView = findViewById(R.id.categoryTitleTextView);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory);

        categoryUrl = getIntent().getStringExtra("categoryUrl");
        categoryTitle = getIntent().getStringExtra("categoryTitle");

        if (categoryUrl == null || categoryUrl.trim().isEmpty()) {
            Log.e(TAG, "Category URL is null or empty!");
            Toast.makeText(this, "Invalid RSS feed URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        categoryTitleTextView.setText(categoryTitle != null ? categoryTitle : "Category");

        newsAdapter = new NewsAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);

        // Fetch articles in background thread
        new Thread(() -> {
            List<NewsItem> articles = RssParserUtils.fetchRssArticles(categoryUrl);

            Log.d(TAG, "Fetched " + (articles != null ? articles.size() : 0) + " articles for category: " + categoryTitle);

            // For debugging: Disable filtering so all articles show up
            List<NewsItem> filteredArticles = articles; // No filtering now

            // Optional: add fallback if no articles found
            if (filteredArticles == null || filteredArticles.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "No articles found for " + categoryTitle, Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> newsAdapter.updateList(filteredArticles));
            }
        }).start();
    }
}
