package com.sismics.music.core.service.spotify;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpotifySongSearch {
    private static final String API_BASE = "https://api.spotify.com/v1";
    private static final String AUTH_BASE = "https://accounts.spotify.com/api/token";

    private String accessToken;

    public SpotifySongSearch() throws IOException, JSONException {
        String clientId = "a45dfd5652a04284958da68afd1d6ec9";
        String clientSecret = "61ae0ae3987a4d81acc276d86587a0ff";
        String encodedAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        String tokenRequestBody = "grant_type=client_credentials";
        String tokenResponse = sendRequest(AUTH_BASE, "POST", tokenRequestBody, encodedAuth);
        JSONObject tokenJson = new JSONObject(tokenResponse);
        this.accessToken = tokenJson.getString("access_token");
    }

    public List<JSONObject> searchSong(String songName, int limit) throws IOException, JSONException {
        String searchEndpoint = API_BASE + "/search?q=" + urlEncode(songName) + "&type=track&limit=" + limit;
        String searchResponse = sendRequest(searchEndpoint, "GET", null, accessToken);
        JSONObject searchJson = new JSONObject(searchResponse);
        JSONArray tracks = searchJson.getJSONObject("tracks").getJSONArray("items");

        List<JSONObject> results = new ArrayList<>();
        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = tracks.getJSONObject(i);
            results.add(track);
        }

        return results;
    }

    private static String sendRequest(String urlString, String method, String requestBody, String accessToken)
            throws IOException {
        java.net.URL url = new java.net.URL(urlString);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        if (accessToken != null) {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

        if (requestBody != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Length", Integer.toString(requestBodyBytes.length));
            conn.getOutputStream().write(requestBodyBytes);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        String response = "";
        Scanner scanner = new Scanner(conn.getInputStream());
        while (scanner.hasNext()) {
            response += scanner.nextLine();
        }
        scanner.close();

        conn.disconnect();
        return response;
    }

    private static String urlEncode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }
}