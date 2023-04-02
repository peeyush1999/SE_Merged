package com.sismics.music.rest.resource;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;

public class SpotifyAuth {

    private String clientId;
    private String clientSecret;

    public SpotifyAuth(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        try {
            // Create a URL object for the token API endpoint
            URL url = new URL("https://accounts.spotify.com/api/token");

            // Create a HTTP connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + getEncodedCredentials());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Set the request body
            String requestBody = "grant_type=client_credentials";
            conn.setDoOutput(true);
            conn.getOutputStream().write(requestBody.getBytes());

            // Read the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the access token from the response
            return response.toString().split("\"access_token\":\"")[1].split("\",\"")[0];

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getEncodedCredentials() {
        String credentials = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}