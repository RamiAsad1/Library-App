package com.example.library_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// This web service uses Googles books API to retrieve information about some books in their database
// here is the full documentation for it:
// https://developers.google.com/books/docs/v1/using

public class BrowseBooksActivity extends AppCompatActivity {
    private final String API_KEY = "AIzaSyAHtqqNBmddvueJommtWN1ElIAbVXamxgs";
    private RequestQueue queue;
    private ListView lstBooks;
    private Spinner spnLanguage;
    private Spinner spnAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        setViews();
        getAllBooks();
    }

    private void setViews() {
        lstBooks = findViewById(R.id.lstBooks);
        spnLanguage = findViewById(R.id.spnLanguage);
        spnAuthor = findViewById(R.id.spnAuthor);
    }

    private void getAllBooks() {
        String url = "https://www.googleapis.com/books/v1/volumes?q=android&key=" + API_KEY;

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            fillSpinners(items);
                            fillListView(items);
                        }catch (JSONException e){
                            Log.e("Volley Error, Get Books", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error, Get Books", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void searchBook(String author, String language) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=";

        if (!author.equals("Any Author")) {
            String parameters ="+inauthor:" + author;
            url += parameters;
        }

        if (!language.equals("Any Language")) {
            String parameters = "+lang=" + language;
            url +=parameters;
        }
        String keyParameter = "&key=" + API_KEY;
        url += keyParameter;

        Log.d("URL",url);
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            fillListView(items);
                            lstBooks.setVisibility(View.VISIBLE);
                        }catch (JSONException e){
                            Log.e("exception", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error, Get Books", error.toString());
                lstBooks.setVisibility(View.GONE);
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void fillSpinners(JSONArray books) {
        List<String> authorsList = new ArrayList<>();
        List<String> languagesList = new ArrayList<>();

        for (int i = 0; i < books.length(); i++) {
            try {
                JSONObject book = books.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");

                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authorsList.add(authorsArray.getString(j));
                    }
                }
                String language = volumeInfo.getString("language");
                languagesList.add(language);


            } catch (JSONException e) {
                Log.d("JSON Exception", e.toString());
            }
        }


        //This is to remove any duplication of authors or languages
        Set<String> authors = new HashSet<>(authorsList);
        Set<String> languages = new HashSet<>(languagesList);

        ArrayAdapter<String> authorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(authors));
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(languages));

        authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnAuthor.setAdapter(authorsAdapter);
        spnLanguage.setAdapter(languagesAdapter);
    }

    private void fillListView(JSONArray books) {
        List<String> booksList = new ArrayList<>();

        for (int i = 0; i < books.length(); i++) {
            try {
                JSONObject book = books.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                booksList.add(title);

            } catch (JSONException e) {
                Log.d("JSON Exception", e.toString());
            }
        }
        ArrayAdapter<String> booksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, booksList);
        lstBooks.setAdapter(booksAdapter);
    }

    public void btnBrowseOnClick(View view) {
        getAllBooks();
        lstBooks.setVisibility(View.VISIBLE);
    }

    public void btnSearchOnClick(View view) {
        //Not all of the search parameters work, but one that does is:
        // Author: Onur Cinar , Language: en
        String chosenAuthor = spnAuthor.getSelectedItem().toString().trim();
        String chosenLanguage = spnLanguage.getSelectedItem().toString().trim();

        searchBook(chosenAuthor,chosenLanguage);
    }
}