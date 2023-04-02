package com.sismics.music.core.service.spotify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpotifyRecommendations {

    private final String BASE_URL = "https://api.spotify.com/v1/";

    public ArrayList<String> getTrackIds(ArrayList<String> songNames, String accessToken)
            throws IOException, JSONException {
        ArrayList<String> trackIds = new ArrayList<String>();
        for (String songName : songNames) {
            String encodedSongName = URLEncoder.encode(songName, StandardCharsets.UTF_8.toString());
            String endpoint = BASE_URL + "search?q=" + encodedSongName + "&type=track&limit=1";
            HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray tracksArray = jsonResponse.getJSONObject("tracks").getJSONArray("items");
                if (tracksArray.length() > 0) {
                    JSONObject trackObject = tracksArray.getJSONObject(0);
                    String trackId = trackObject.getString("id");
                    trackIds.add(trackId);
                }
            } else {
                throw new IOException("Error: " + responseCode);
            }
            connection.disconnect();
        }
        return trackIds;
    }

    public ArrayList<String> recommendTrackIds(ArrayList<String> trackIds, String accessToken)
            throws IOException, JSONException {
        ArrayList<String> recommendedTrackIds = new ArrayList<String>();
        String endpoint = BASE_URL + "recommendations?seed_tracks=";
        for (int i = 0; i < trackIds.size(); i++) {
            endpoint += trackIds.get(i);
            if (i < trackIds.size() - 1) {
                endpoint += ",";
            }
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray tracksArray = jsonResponse.getJSONArray("tracks");
            for (int i = 0; i < tracksArray.length(); i++) {
                JSONObject trackObject = tracksArray.getJSONObject(i);
                String trackId = trackObject.getString("id");
                recommendedTrackIds.add(trackId);
            }
        } else {
            throw new IOException("Error: " + responseCode);
        }
        connection.disconnect();
        return recommendedTrackIds;
    }

    public ArrayList<String> getSongInfo(ArrayList<String> trackIds, String accessToken)
            throws IOException, JSONException {
        ArrayList<String> songInfoList = new ArrayList<String>();
        String endpoint = BASE_URL + "tracks?ids=";
        for (int i = 0; i < trackIds.size(); i++) {
            endpoint += trackIds.get(i);
            if (i < trackIds.size() - 1) {
                endpoint += ",";
            }
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray tracksArray = jsonResponse.getJSONArray("tracks");
            for (int i = 0; i < tracksArray.length(); i++) {
                JSONObject trackObject = tracksArray.getJSONObject(i);
                String songName = trackObject.getString("name");
                String artistName = trackObject.getJSONArray("artists").getJSONObject(0).getString("name");
                String songInfo = songName + " by " + artistName;
                songInfoList.add(songInfo);
            }
        } else {
            throw new IOException("Error: " + responseCode);
        }
        connection.disconnect();
        return songInfoList;
    }

}