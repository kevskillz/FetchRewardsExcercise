package com.example.fetchrewardsexcercise;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        if (!isNetworkAvailable()) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

        executorService.execute(() -> {
            String jsonResponse = fetchJsonData();
            if (jsonResponse == null) {
                mainThreadHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.VISIBLE);
                });
                return;
            }

            List<Item> items = processAndSortData(jsonResponse);
            Map<Integer, List<Item>> groupedItems = groupItemsByListId(items);

            mainThreadHandler.post(() -> {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                displayItems(groupedItems);
            });
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private String fetchJsonData() {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (Exception e) {
            Log.e("MainActivity", "Error fetching JSON data", e);
            return null;
        }
        return response.toString();
    }

    private List<Item> processAndSortData(String jsonResponse) {
        List<Item> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int listId = jsonObject.getInt("listId");
                String name = jsonObject.optString("name", "");

                if (!name.equals("null") && !name.trim().isEmpty()) {
                    items.add(new Item(id, listId, name));
                }
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Error parsing JSON data", e);
        }

        items.sort(Comparator.comparing(Item::getListId).thenComparing(Item::getName));
        return items;
    }

    private Map<Integer, List<Item>> groupItemsByListId(List<Item> items) {
        Map<Integer, List<Item>> groupedItems = new HashMap<>();
        for (Item item : items) {
            groupedItems.computeIfAbsent(item.getListId(), k -> new ArrayList<>()).add(item);
        }
        return groupedItems;
    }

    private void displayItems(Map<Integer, List<Item>> groupedItems) {
        ExpandableAdapter adapter = new ExpandableAdapter(groupedItems);
        recyclerView.setAdapter(adapter);
    }
}
