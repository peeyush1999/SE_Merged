package com.sismics.music.core.service.lastfm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class LastfmSongSearch {
    private String apiKey;
    private String baseUrl;

    public LastfmSongSearch() {
        this.apiKey = "aac9268580d78ff419b26625d1150db3";
        this.baseUrl = "http://ws.audioscrobbler.com/2.0/";
    }

    public List<JSONObject> searchSong(String songName, int limit) throws IOException {
        String method = "track.search";
        String urlString = baseUrl + "?method=" + method + "&track=" + songName + "&api_key=" + apiKey
                + "&format=json&limit=" + limit;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        String json = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            json += scanner.nextLine();
        }
        scanner.close();

        JSONObject response = new JSONObject(json);
        JSONArray tracks = response.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");

        List<JSONObject> results = new ArrayList<>();
        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = tracks.getJSONObject(i);
            results.add(track);
        }

        return results;
    }
}