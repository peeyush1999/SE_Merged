package com.sismics.music.core.service.spotify;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;
import java.lang.StringBuffer;

public class SpotifyAuth {

    private String clientId;
    private String clientSecret;

    public SpotifyAuth() {
        this.clientId = "a45dfd5652a04284958da68afd1d6ec9";
        this.clientSecret = "61ae0ae3987a4d81acc276d86587a0ff";
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