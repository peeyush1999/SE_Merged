angular.module('music').controller('LastFmSearch', function ($rootScope, $scope, $http, $state, $dialog, User, Playlist, NamedPlaylist, Websocket, Restangular) {
    function search_by_album(albumName) {

        // Set the API method and parameters
        var method = "album.search";
        var apiKey = 'aac9268580d78ff419b26625d1150db3';
        var apiUrl = "https://ws.audioscrobbler.com/2.0/";

        // Generate the API signature
        // var signature = generateSignature({
        //     method: method,
        //     api_key: apiKey,
        //     album: albumName
        // });

        // Make an API request using the $http service
        return $http({
            method: "GET",
            url: apiUrl,
            params: {
                method: method,
                api_key: apiKey,
                album: albumName,
                // api_sig: signature,
                format: "json"
            }
        }).then(function (response) {
            // Return the API response data 
            // will work on data to change represantation accordingly.
            return response.data;
        }).catch(function (error) {
            // Handle the API error
            console.log(error);
        });
    }

    // function to search track
    function search_by_track(trackName) {

        // Set the API method and parameters
        var method = "track.search";
        var apiKey = 'aac9268580d78ff419b26625d1150db3';
        var apiUrl = "https://ws.audioscrobbler.com/2.0/";

        // Generate the API signature
        // var signature = generateSignature({
        //     method: method,
        //     api_key: apiKey,
        //     track: trackName
        // });

        // Make an API request using the $http service
        return $http({
            method: "GET",
            url: apiUrl,
            params: {
                method: method,
                api_key: apiKey,
                track: trackName,
                // api_sig: signature,
                format: "json"
            }
        }).then(function (response) {
            // Return the API response data 
            // will work on data to change represantation accordingly.
            return response.data;
        }).catch(function (error) {
            // Handle the API error
            console.log(error);
        });
    }

    // A function to search for artist
    function search_by_artist(artistName) {
        // Set the API method and parameters
        var method = "artist.search";
        var apiKey = 'aac9268580d78ff419b26625d1150db3';
        var apiUrl = "https://ws.audioscrobbler.com/2.0/";

        // Generate the API signature
        // var signature = generateSignature({
        //     method: method,
        //     api_key: apiKey,
        //     artist: artistName
        // });

        // Make an API request using the $http service
        return $http({
            method: "GET",
            url: apiUrl,
            params: {
                method: method,
                api_key: apiKey,
                artist: artistName,
                // api_sig: signature,
                format: "json"
            }
        }).then(function (response) {
            // Return the API response data
            return response.data;
        }).catch(function (error) {
            // Handle the API error
            console.log(error);
        });
    }

    //search for a tag will response all tagged album sorted by tag count
    function search_by_tag(tagName) {
        // Set the API method and parameters
        var method = "tag.gettopalbums";
        var apiKey = 'aac9268580d78ff419b26625d1150db3';
        var apiUrl = "https://ws.audioscrobbler.com/2.0/";

        // Generate the API signature
        // var signature = generateSignature({
        //     method: method,
        //     api_key: apiKey,
        //     tag: tagName
        // });

        // Make an API request using the $http service
        return $http({
            method: "GET",
            url: apiUrl,
            params: {
                method: method,
                api_key: apiKey,
                tag: tagName,
                // api_sig: signature,
                format: "json"
            }
        }).then(function (response) {
            // Return the API response data
            return response.data;
        }).catch(function (error) {
            // Handle the API error
            console.log(error);
        });
    }

    // function generateSignature(params) {
    //     var keys = Object.keys(params);
    //     keys.sort();
    //     var signature = "";
    //     for (var i = 0; i < keys.length; i++) {
    //         signature += keys[i] + params[keys[i]];
    //     }
    //     signature += 'aac9268580d78ff419b26625d1150db3';
    //     signature = CryptoJS.MD5(encodeURIComponent(signature));
    //     return signature;
    // }

    //********************|  sample for search and call the functions respectvely   |******* */

    // Define a variable to store the search results

    $scope.albums = [];

    // Call the search_by_album function and update the albums variable
    // function searchAlbums() {
    //     search_by_album($scope.searchTerm).then(function (response) {
    //         $scope.albums = response.results.albummatches.album;
    //     }).catch(function (error) {
    //         console.log(error);
    //     });
    // }

    // Call to related search functions when the search button is clicked 
    $scope.search = function (term, type) {
        if (type === 'album') {
            search_by_album(term)
                .then(function (albums) {
                    $scope.albums = albums.results.albummatches.album;
                });
        } else if (type === 'artist') {
            search_by_artist(term)
                .then(function (artists) {
                    $scope.artists = artists.results.artistmatches.artist;
                });
        } else if (type === 'tag') {
            search_by_tag(term)
                .then(function (tags) {
                    $scope.tags = tags.results.tagmatches.tag;
                });
        } else if (type === 'track') {
            search_by_track(term)
                .then(function (tracks) {
                    $scope.tracks = tracks.results.trackmatches.track;
                });
        }
    }
});