package com.example.library_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String REMEMBER = "REMEMBER";
    private boolean flag = false;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText edtSignInUsername;
    private EditText edtSignInPass;
    private TextView txtErrorMSG;
    private CheckBox chkRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setupPreferences();
        checkPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = preferences.getString(USERNAME, "");
        String password = preferences.getString(PASSWORD, "");
        edtSignInUsername.setText(username);
        edtSignInPass.setText(password);
    }

    private void setViews() {
        edtSignInUsername = findViewById(R.id.edtSignInUsername);
        edtSignInPass = findViewById(R.id.edtSignInPass);
        txtErrorMSG = findViewById(R.id.txtErrorMSG);
        chkRemember = findViewById(R.id.chkRemember);
    }

    private void setupPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    private void checkPreferences() {
        flag = preferences.getBoolean(REMEMBER, false);

        if (flag) {
            String username = preferences.getString(USERNAME, "");
            String password = preferences.getString(PASSWORD, "");
            edtSignInUsername.setText(username);
            edtSignInPass.setText(password);
            chkRemember.setChecked(true);
        } else {
            edtSignInUsername.setText("");
            edtSignInPass.setText("");
            chkRemember.setChecked(false);
        }
    }

    private boolean authenticateUser(String username, String password) {
        String storedUsername = preferences.getString(USERNAME, "");
        String storedPassword = preferences.getString(PASSWORD, "");

        if (username.equals("") || password.equals("") || storedUsername.equals("") || storedPassword.equals("")) {
            return false;
        }

        if (storedUsername.equals(username) && storedPassword.equals(password)) {
            return true;
        }
        return false;
    }

    public void btnSignInOnClick(View view) {
        String username = edtSignInUsername.getText().toString();
        String password = edtSignInPass.getText().toString();

        if (authenticateUser(username, password)) {
            txtErrorMSG.setVisibility(View.GONE);//maybe remove it later
            Intent intent2 = new Intent(this,HomePageActivity.class);
            startActivity(intent2);
        } else {
            if (!username.isEmpty() && !password.isEmpty()) {
                if (chkRemember.isChecked()) {
                    if (!flag) {
                        editor.putString(USERNAME, username);
                        editor.putString(PASSWORD, password);
                        editor.putBoolean(REMEMBER, true);
                        editor.apply();
                    }
                }
            } else {
                txtErrorMSG.setVisibility(View.VISIBLE);
            }
        }
    }

    public void btnSignUpOnClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}