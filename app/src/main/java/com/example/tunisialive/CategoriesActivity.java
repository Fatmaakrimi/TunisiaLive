package com.example.tunisialive;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.HashMap;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity {

    private ListView categoryListView;

    // Map categories to their RSS feed URLs
    private static final Map<String, String> CATEGORY_URLS = new HashMap<>() {{
        put("Politique", "https://kapitalis.com/tunisie/category/politique/feed/");
        put("Économie", "https://kapitalis.com/tunisie/category/economie/feed/");
        put("Sport", "https://kapitalis.com/tunisie/category/sport/feed/");
        put("Culture", "https://kapitalis.com/tunisie/category/culture/feed/");
        put("Santé", "https://kapitalis.com/tunisie/category/sante/feed/");
    }};

    private String[] categories = {
            "Politique", "Économie", "Sport", "Culture", "Santé",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // Set up the toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize the ListView
        categoryListView = findViewById(R.id.categoryListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                categories
        );
        categoryListView.setAdapter(adapter);

        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = categories[position];
            String url = CATEGORY_URLS.get(selectedCategory);

            Intent intent = new Intent(CategoriesActivity.this, CategoryNewsActivity.class);
            intent.putExtra("categoryTitle", selectedCategory);
            intent.putExtra("categoryUrl", url);
            startActivity(intent);
        });
    }

    // Handle back arrow click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Closes this activity and returns to the previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
