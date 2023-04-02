package com.sismics.music.rest.resource;

import com.sismics.music.core.dao.dbi.LocaleDao;
import com.sismics.music.core.model.dbi.Locale;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
// import javax.json.JsonValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.util.List;

import javax.ws.rs.QueryParam;

import java.io.IOException;
import java.util.ArrayList;
// import java.util.Arrays;

import com.sismics.music.core.service.lastfm.LastFMRecommendation;
import com.sismics.music.core.service.spotify.SpotifyAuth;
import com.sismics.music.core.service.spotify.SpotifyRecommendations;
import com.sismics.music.core.model.context.AppContext;

/**
 * Locale REST resources.
 * 
 * @author jtremeaux
 */
@Path("/locale")
public class LocaleResource extends BaseResource {

    // private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Returns the list of all locales.
     * 
     * @return Response
     */
    @GET
    public Response list() {
        LocaleDao localeDao = new LocaleDao();
        List<Locale> localeList = localeDao.findAll();
        JsonObjectBuilder response = Json.createObjectBuilder();
        JsonArrayBuilder items = Json.createArrayBuilder();
        for (Locale locale : localeList) {
            items.add(Json.createObjectBuilder()
                    .add("id", locale.getId()));
        }
        response.add("locales", items);
        return renderJson(response);
    }

    @GET
    @Path("getData")
    public Response getData(@QueryParam("songs") String songs) throws JSONException, IOException {
        JsonObjectBuilder response = Json.createObjectBuilder();
        final SpotifyAuth spotifyAuth = AppContext.getInstance().getSpotifyAuth();
        final SpotifyRecommendations spotifyRecommendations = AppContext.getInstance().getSpotifyRecommendations();
        // Retrieve an access token
        String accessToken = spotifyAuth.getAccessToken();

        // Get track IDs for some songs
        ArrayList<String> songNames = new ArrayList<String>();

        songNames.add(songs);

        // songNames.add("Bohemian Rhapsody");
        // songNames.add("Stairway to Heaven");
        ArrayList<String> trackIds = spotifyRecommendations.getTrackIds(songNames, accessToken);

        // Get recommendations based on the track IDs
        ArrayList<String> recommendedTrackIds = spotifyRecommendations.recommendTrackIds(trackIds, accessToken);

        // Get song information for the recommended track IDs
        ArrayList<String> songInfoList = spotifyRecommendations.getSongInfo(recommendedTrackIds, accessToken);
        
        for (String element : songInfoList) {
            response.add("track", element);
        }
        // response.add("tracks", songInfoList);
        // response.add("tracks", songs);

        return renderJson(response);
    }

    @GET
    @Path("getFmData")
    public Response getFmData(@QueryParam("songs") String songs, @QueryParam("artist") String artist) {
        JsonObjectBuilder response = Json.createObjectBuilder();
        final LastFMRecommendation lastfm = AppContext.getInstance().getLastFMRecommendation();
        try {
            List<String> recommendedSongs = lastfm.getRecommendedSongs(songs, artist);
            for (String element : recommendedSongs) {
                response.add("track", element);
            }
        } catch (IOException e) {
            response.add("track", "IOEXCE{TION");

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            response.add("track", "SAXEXCEPTION");

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            response.add("track", "parserException");

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // response.add("tracks", "new songs");
        return renderJson(response);
    }
}
