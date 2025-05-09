package com.example.tunisialive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode, switchNotifications;
    private Spinner spinnerRssSource;
    private Button btnSaveSettings;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "AppSettings";
    private static final String DARK_MODE_KEY = "dark_mode";
    private static final String NOTIFICATIONS_KEY = "notifications";
    private static final String RSS_SOURCE_KEY = "rss_source";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Lire les préférences avant affichage
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);

        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialisation des vues
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        spinnerRssSource = findViewById(R.id.spinnerRssSource);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // Charger les préférences existantes dans les composants
        loadPreferences();

        // Écouteur du switch pour appliquer directement le thème
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Sauvegarder les paramètres au clic
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });
    }

    private void loadPreferences() {
        switchDarkMode.setChecked(sharedPreferences.getBoolean(DARK_MODE_KEY, false));
        switchNotifications.setChecked(sharedPreferences.getBoolean(NOTIFICATIONS_KEY, true));
        spinnerRssSource.setSelection(sharedPreferences.getInt(RSS_SOURCE_KEY, 0));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DARK_MODE_KEY, switchDarkMode.isChecked());
        editor.putBoolean(NOTIFICATIONS_KEY, switchNotifications.isChecked());
        editor.putInt(RSS_SOURCE_KEY, spinnerRssSource.getSelectedItemPosition());
        editor.apply();

        Toast.makeText(this, "Paramètres enregistrés avec succès !", Toast.LENGTH_SHORT).show();
    }
}
