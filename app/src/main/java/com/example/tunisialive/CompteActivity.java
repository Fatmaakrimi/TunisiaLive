package com.example.tunisialive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CompteActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView txtNom, txtEmail;
    private Button btnLogin, btnRegister, btnLogout;
    private ImageView profileImage;
    private LinearLayout userInfoContainer;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        // Initialize Views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        txtNom = findViewById(R.id.txtNom);
        txtEmail = findViewById(R.id.txtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogout = findViewById(R.id.btnLogout);
        profileImage = findViewById(R.id.profileImage);
        userInfoContainer = findViewById(R.id.userInfoContainer);

        // Setup toolbar and back button
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // User is logged in
            userInfoContainer.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);

            txtNom.setText("Nom : " + (user.getDisplayName() != null ? user.getDisplayName() : "Non disponible"));
            txtEmail.setText("Email : " + user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(profileImage);
            }

            btnLogout.setOnClickListener(v -> {
                mAuth.signOut();
                recreate(); // Refresh to update UI
            });

        } else {
            // User not logged in
            userInfoContainer.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
        }

        // Image picker
        profileImage.setOnClickListener(v -> openImageChooser());

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CompteActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(CompteActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            // Optional: Upload to Firebase Storage if needed
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Handle back arrow click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
