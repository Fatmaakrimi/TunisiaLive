package com.example.tunisialive;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryNewsDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private TextView categoryTitleTextView;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news_detail);

        categoryTitleTextView = findViewById(R.id.categoryTitleTextView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the category from Intent
        category = getIntent().getStringExtra("category");
        if (category == null) category = "";

        // Debug log
        Log.d("CategoryActivity", "Received category: " + category);

        categoryTitleTextView.setText("Articles dans : " + category);

        // Get dummy data
        List<NewsItem> allArticles = getAllArticles();

        // Filter the articles by category
        List<NewsItem> filteredArticles = filterArticlesByCategory(allArticles, category);

        // Debug log
        Log.d("CategoryActivity", "Filtered articles count: " + filteredArticles.size());

        // Set adapter
        adapter = new NewsAdapter(this, filteredArticles);
        recyclerView.setAdapter(adapter);
    }

    private List<NewsItem> getAllArticles() {
        List<NewsItem> dummyList = new ArrayList<>();

        NewsItem item1 = new NewsItem();
        item1.setCategory("Politique"); // make sure exact spelling
        item1.setTitle("Political News 1");
        dummyList.add(item1);

        NewsItem item2 = new NewsItem();
        item2.setCategory("Sport");
        item2.setTitle("Sports News 1");
        dummyList.add(item2);

        NewsItem item3 = new NewsItem();
        item3.setCategory("Politique");
        item3.setTitle("Political News 2");
        dummyList.add(item3);

        return dummyList;
    }

    private List<NewsItem> filterArticlesByCategory(List<NewsItem> articles, String category) {
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem article : articles) {
            Log.d("FilterCheck", "Checking article: " + article.getTitle() + " in category: " + article.getCategory());
            if (article.getCategory() != null && article.getCategory().equalsIgnoreCase(category)) {
                filtered.add(article);
            }
        }
        return filtered;
    }
}
