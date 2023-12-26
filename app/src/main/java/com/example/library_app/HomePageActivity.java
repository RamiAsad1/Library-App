package com.example.library_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {
    public static final String USERNAME = "USERNAME";
    private TextView txtHomePageWelcome;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        setupPreferences();
        setViews();

    }

    private void setupPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    private void setViews() {
        txtHomePageWelcome = findViewById(R.id.txtHomePageWelcome);
        String storedUsername = preferences.getString(USERNAME, "");
        txtHomePageWelcome.setText("Welcome " + storedUsername + "!");
    }

    public void btnBrowseBooksOnClick(View view) {
        Intent intent4 = new Intent(this,BrowseBooksActivity.class);
        startActivity(intent4);
    }

    public void btnFindLibraries(View view) {
        Intent intent3 = new Intent(this, LibraryMapActivity.class);
        startActivity(intent3);
    }
}