package com.example.library_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText edtSignUpUsername;
    private EditText edtSignUpPass;
    private TextView txtErrorMSG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setViews();
        setupPreferences();
    }

    private void setViews() {
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPass = findViewById(R.id.edtSignUpPass);
        txtErrorMSG = findViewById(R.id.txtErrorMSG);
    }

    private void setupPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    public void btnCreateOnClick(View view) {
        String username = edtSignUpUsername.getText().toString();
        String password = edtSignUpPass.getText().toString();
        if (!username.isEmpty() && !password.isEmpty()) {
            txtErrorMSG.setVisibility(View.GONE);
            editor.putString(USERNAME, username);
            editor.putString(PASSWORD, password);
            editor.apply();

            finish();
        } else {
            txtErrorMSG.setVisibility(View.VISIBLE);
        }

    }
}