package com.sismics.music.core.service.lastfm;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LastFMRecommendation {
    private final String apiKey;
    private final String apiEndpoint;

    public LastFMRecommendation() {
        this.apiKey = "aac9268580d78ff419b26625d1150db3";
        this.apiEndpoint = "http://ws.audioscrobbler.com/2.0/";
    }

    public List<String> getRecommendedSongs(String songs, String artist)
            throws IOException, SAXException, ParserConfigurationException {
        List<String> recommendedSongs = new ArrayList<>();
        String[] songNames = { songs };
        for (String songName : songNames) {
            String url = apiEndpoint + "?method=track.getsimilar"
                    + "&artist=" + URLEncoder.encode(artist, "UTF-8")
                    + "&track=" + URLEncoder.encode(songName, "UTF-8")
                    + "&api_key=" + apiKey;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new URL(url).openStream());
            Element rootElement = document.getDocumentElement();
            NodeList trackNodes = rootElement.getElementsByTagName("track");
            for (int i = 0; i < trackNodes.getLength(); i++) {
                Element trackElement = (Element) trackNodes.item(i);
                String recommendedSongName = trackElement.getElementsByTagName("name").item(0).getTextContent();
                recommendedSongs.add(recommendedSongName);
            }
        }
        return recommendedSongs;
    }
}
