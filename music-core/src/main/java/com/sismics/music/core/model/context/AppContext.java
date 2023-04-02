
package com.sismics.music.core.model.context;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.sismics.music.core.listener.async.*;
import com.sismics.music.core.listener.sync.DeadEventListener;
import com.sismics.music.core.service.albumart.AlbumArtService;
import com.sismics.music.core.service.collection.CollectionService;
import com.sismics.music.core.service.collection.CollectionWatchService;
import com.sismics.music.core.service.importaudio.ImportAudioService;
import com.sismics.music.core.service.lastfm.LastFmService;
import com.sismics.music.core.service.lastfm.LastFMRecommendation;
import com.sismics.music.core.service.lastfm.LastfmSongSearch;

import com.sismics.music.core.service.player.PlayerService;
import com.sismics.music.core.service.transcoder.TranscoderService;
import com.sismics.util.EnvironmentUtil;

import com.sismics.music.core.service.spotify.SpotifyAuth;
import com.sismics.music.core.service.spotify.SpotifyRecommendations;

import com.sismics.music.core.service.spotify.SpotifySongSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

/**
 * Global application context.
 *
 * @author jtremeaux
/**
 * Global application context.
 *
 * @author jtremeaux 
 */
public class AppContext {
    /**
     * Singleton instance.
     */
    private static AppContext instance;

    /**
     * Event bus.
     */
    private EventBus eventBus;

    
    /**
     * Generic asynchronous event bus.
     */
    private EventBus asyncEventBus;

    /**
     * Collection indexing asynchronous event bus.
     */
    private EventBus collectionEventBus;

    /**
     * Do all Last.fm operations in this thread, because of rate limitation.
     */
    private EventBus lastFmEventBus;

    /**
     * Collection service.
     */
    private CollectionService collectionService;

    
    /**
     * Collection watch service.
     */
    private CollectionWatchService collectionWatchService;

    /**
     * Import audio service.
     */
    private ImportAudioService importAudioService;

    
    /**
     * Album art service.
     */
    private AlbumArtService albumArtService;

    /**
     * Last.fm service.
     */
    private LastFmService lastFmService;

    private LastFMRecommendation lastFMRecommendation;
    private LastfmSongSearch lastfmSongSearch;
    private SpotifySongSearch spotifySongSearch;
    /**
     * spotifyAuth.
     */
    private SpotifyAuth spotifyAuth;
    /**
     * SpotifyRecommendations.
     */
    private SpotifyRecommendations spotifyRecommendations;

    /**
     * Player service.
     */
    private PlayerService playerService;

    /**
     * Transcoder service.
     */
    private TranscoderService transcoderService;

    /**
     * Asynchronous executors.
     */
    private List<ExecutorService> asyncExecutorList;

    /**
     * Private constructor.
     * @throws IOException
     * @throws JSONException
    
    /**
     * Private constructor.
     */
    private AppContext() {
        resetEventBus();

        collectionService = new CollectionService();
        collectionService.startAsync();

        collectionWatchService = new CollectionWatchService();
        collectionWatchService.startAsync();

        importAudioService = new ImportAudioService();
        importAudioService.startAsync();

        albumArtService = new AlbumArtService();
        lastFmService = new LastFmService();
        lastFMRecommendation = new LastFMRecommendation();
        lastfmSongSearch = new LastfmSongSearch();
        //spotifySongSearch = new SpotifySongSearch();
        spotifyRecommendations = new SpotifyRecommendations();
        spotifyAuth = new SpotifyAuth();
        playerService = new PlayerService();
        transcoderService = new TranscoderService();
    }

    /**
     * (Re)-initializes the event buses.
     */
    private void resetEventBus() {
        eventBus = new EventBus();
        eventBus.register(new DeadEventListener());

        asyncExecutorList = new ArrayList<>();

        asyncEventBus = newAsyncEventBus();

        collectionEventBus = newAsyncEventBus();
        collectionEventBus.register(new DirectoryCreatedAsyncListener());
        collectionEventBus.register(new DirectoryDeletedAsyncListener());
        collectionEventBus.register(new CollectionReindexAsyncListener());

        lastFmEventBus = newAsyncEventBus();
        lastFmEventBus.register(new PlayStartedAsyncListener());
        lastFmEventBus.register(new PlayCompletedAsyncListener());
        lastFmEventBus.register(new LastFmUpdateTrackPlayCountAsyncListener());
        lastFmEventBus.register(new LastFmUpdateLovedTrackAsyncListener());
        lastFmEventBus.register(new TrackLikedAsyncListener());
        lastFmEventBus.register(new TrackUnlikedAsyncListener());
    }

    /**
     * Returns a single instance of the application context.
     * 
     * @return Application context
     * @throws IOException
     * @throws JSONException
     */
    public static AppContext getInstance()  {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }
    /**
     * Creates a new asynchronous event bus.
     * 
     * @return Async event bus
     */
    private EventBus newAsyncEventBus() {
        if (EnvironmentUtil.isUnitTest()) {
            return new EventBus();
        } else {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            asyncExecutorList.add(executor);
            return new AsyncEventBus(executor);
        }
    }

    /**
     * Getter of eventBus.
     *
     * @return eventBus
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Getter of collectionService.
     *
     * @return collectionService
     */
    public CollectionService getCollectionService() {
        return collectionService;
    }
    /**
     * Getter of collectionWatchService.
     *
     * @return the collectionWatchService
     */
    public CollectionWatchService getCollectionWatchService() {
        return collectionWatchService;
    }

    /**
     * Getter of albumArtService.
     *
     * @return albumArtService
     */
    public AlbumArtService getAlbumArtService() {
        return albumArtService;
    }

    /**
     * Getter of lastFmService.
     *
     * @return lastFmService
     */
    public LastFmService getLastFmService() {
        return lastFmService;
    }
     
    public LastFMRecommendation getLastFMRecommendation() {
        return lastFMRecommendation;
    }
    
    public LastfmSongSearch getLastfmSongSearch() {
        return lastfmSongSearch;
    }
    
    public SpotifySongSearch getSpotifySongSearch() {
        return spotifySongSearch;
    }
    /**
     * Getter of SpotifyAuth.
     *
     * @return spotifyAuth
     */
    public SpotifyAuth getSpotifyAuth() {
        return spotifyAuth;
    }

    /**
     * Getter of SpotifyRecommendations.
     *
     * @return spotifSpotifyRecommendationsyAuth
     */
    public SpotifyRecommendations getSpotifyRecommendations() {
        return spotifyRecommendations;
    }

    /**
     * Getter of transcoderService.
     *
     * @return transcoderService
     */
    public TranscoderService getTranscoderService() {
        return transcoderService;
    }

    /**
     * Getter of playerService.
     *
     * @return playerService
     */
    public PlayerService getPlayerService() {
        return playerService;
    }

    /**
     * Getter of asyncEventBus.
     *
     * @return asyncEventBus
     */
    public EventBus getAsyncEventBus() {
        return asyncEventBus;
    }

    /**
     * Getter of collectionEventBus.
     *
     * @return collectionEventBus
     */
    public EventBus getCollectionEventBus() {
        return collectionEventBus;
    }

    /**
     * Getter of lastFmEventBus.
     *
     * @return lastFmEventBus
     */
    public EventBus getLastFmEventBus() {
        return lastFmEventBus;
    }

    /**
     * Getter of importAudioService.
     *
     * @return the importAudioService
     */
    public ImportAudioService getImportAudioService() {
        return importAudioService;
    }
}
