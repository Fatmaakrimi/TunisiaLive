package com.example.tunisialive;

import static com.example.tunisialive.R.id.progressBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private NewsViewModel viewModel;
    private ProgressBar progressBar;
    private List<NewsItem> allNewsList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private TextView userName, userEmail;
    private NewsAdapter articleAdapter;
    private List<NewsItem> articleList, filteredList;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNotificationPermissionIfNeeded();

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);
        Log.d("DEBUG_NEWS", "L'application a démarré !");
        // Get and log Firebase Messaging token (optional)
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Log.d("FCM_TOKEN", "Token: " + token);
            } else {
                Log.w("FCM_TOKEN", "Fetching FCM token failed", task.getException());
            }
        });


        // Lancement du Worker périodique pour les articles
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(RssCheckWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("CheckNewArticlesWork", ExistingPeriodicWorkPolicy.KEEP, request);

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, CompteActivity.class));
                return true;
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(this, CategoriesActivity.class));
                return true;
            }else if (id ==R.id.nav_rss){
                startActivity(new Intent(MainActivity.this, ResourcesActivity.class));
                return true;}
            return false;
        });

        // Toolbar + Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Header user info+
        firebaseAuth = FirebaseAuth.getInstance();
        updateNavHeader(navigationView);

        // RecyclerView + ViewModel
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        viewModel.getNews().observe(this, newsList -> {
            Log.d("DEBUG_UI", "Mise à jour UI avec " + (newsList != null ? newsList.size() : 0) + " articles");
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (newsList != null && !newsList.isEmpty()) {
                adapter = new NewsAdapter(this, newsList);
                recyclerView.setAdapter(adapter);
            } else {
                Log.e("DEBUG_UI", "Aucun article récupéré !");
            }
        });
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS")
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.POST_NOTIFICATIONS"},
                        REQUEST_NOTIFICATION_PERMISSION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateNavHeader(NavigationView navigationView) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            userName = navigationView.getHeaderView(0).findViewById(R.id.userName);
            userEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmail);
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Déjà sur l'accueil
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, CompteActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_resources) {
            startActivity(new Intent(this, ResourcesActivity.class));
        } else if (id == R.id.nav_categories) {
            startActivity(new Intent(this, CategoriesActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void updateUI(List<NewsItem> newsList) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        if (newsList != null) {
            adapter = new NewsAdapter(this, newsList);
            recyclerView.setAdapter(adapter);
        }
    }

    private void filterNews(String query) {
        if (adapter == null) {
            Log.e("DEBUG_SEARCH", "L'Adapter est null, impossible de filtrer !");
            return;
        }

        List<NewsItem> filteredList = new ArrayList<>();
        if (!TextUtils.isEmpty(query)) {
            for (NewsItem item : viewModel.getNews().getValue()) {
                if (item.getLink().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                    Log.d("Debug_search_article:", item.getTitle());
                }
            }
        } else if (filteredList.isEmpty()) {
            filteredList.addAll(viewModel.getNews().getValue());
        }

        Log.d("DEBUG_SEARCH", "Nombre d'articles après filtrage : " + filteredList.size());
        adapter.updateList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, "Recherche : " + query, Toast.LENGTH_SHORT).show();
                    filterNews(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return true;
    }

    private void filterArticles(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(articleList);
        } else {
            for (NewsItem article : articleList) {
                if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(article);
                }
            }
        }
        articleAdapter.notifyDataSetChanged();
    }
}
