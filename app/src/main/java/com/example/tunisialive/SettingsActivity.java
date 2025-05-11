package com.example.tunisialive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode, switchNotifications;
    private Button btnSaveSettings;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme before UI is set
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 🔙 Set up the toolbar with back arrow
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Paramètres");
        }

        // Initialize views
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // Load saved preferences
        loadPreferences();

        // Handle dark mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate(); // Restart activity to apply theme
        });

        // Save button click
        btnSaveSettings.setOnClickListener(v -> savePreferences());
    }

    private void loadPreferences() {
        switchDarkMode.setChecked(sharedPreferences.getBoolean("dark_mode", false));
        switchNotifications.setChecked(sharedPreferences.getBoolean("notifications", true));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", switchNotifications.isChecked());


        Toast.makeText(this, "Paramètres enregistrés avec succès !", Toast.LENGTH_SHORT).show();
    }

    // 🔙 Handle toolbar back arrow click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back when arrow is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
