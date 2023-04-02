package com.sismics.music.rest.resource;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
// import java.util.Base64;
import java.util.List;

public class SpotifyRecommendations {

    private String accessToken;

    public SpotifyRecommendations(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRecommendations(List<String> songSeeds, List<String> artistSeeds) {
        try {
            // Build the request URL with the seed songs and artists
            String requestUrl = "https://api.spotify.com/v1/recommendations?"
                    + "seed_tracks=" + String.join(",", songSeeds)
                    + "&seed_artists=" + String.join(",", artistSeeds)
                    + "&limit=10";

            // Create a URL object from the request URL
            URL url = new URL(requestUrl);

            // Create a HTTP connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method and headers
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Read the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Return the response as a string
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}