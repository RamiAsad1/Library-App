package com.example.library_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

// This web service uses the OpenStreetMap Nominatim API to show the location of libraries (Shown by a marker)
// , here is the full documentation for it:
// https://nominatim.org/release-docs/develop/api/Overview/
// The api uses the Nominatim search engine, which can be found here:
// https://nominatim.openstreetmap.org/ui/search.html?

public class LibraryMapActivity extends AppCompatActivity {
    private RequestQueue queue;
    private MapView mapViewLibraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_map);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setViews();
        getAllLibraries();
    }

    private void setViews() {
        mapViewLibraries = findViewById(R.id.mapViewLibraries);
        mapViewLibraries.setTileSource(TileSourceFactory.MAPNIK);
        mapViewLibraries.getController().setZoom(15.0);
        mapViewLibraries.getController().setCenter(new GeoPoint(31.9038, 35.2034));
    }

    private void getAllLibraries() {
        String url = "https://nominatim.openstreetmap.org/search?q=library,ramallah&format=json";
        //This will only show libraries in Ramallah.

        queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                addLibraryMarkersToMap(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void addLibraryMarkersToMap(JSONArray libraries) {

        for (int i = 0; i < libraries.length(); i++) {
            try {
                JSONObject library = libraries.getJSONObject(i);
                String name = library.getString("display_name");
                double latitude = library.getDouble("lat");
                double longitude = library.getDouble("lon");

                GeoPoint libraryLocationOnMap = new GeoPoint(latitude, longitude);
                Marker libraryMarker = new Marker(mapViewLibraries);
                libraryMarker.setPosition(libraryLocationOnMap);
                libraryMarker.setTitle(name);

                mapViewLibraries.getOverlays().add(libraryMarker);
            } catch (JSONException e) {
                Log.d("JSON Exception", e.toString());
            }
        }
    }

}