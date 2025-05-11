package com.example.tunisialive;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class TunisiaLiveApp extends Application {
    private static final String TAG = "TunisiaLiveApp";

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize Firebase App Check with Play Integrity
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance());
        
        // Enable token auto-refresh
        firebaseAppCheck.setTokenAutoRefreshEnabled(true);
    }
} 