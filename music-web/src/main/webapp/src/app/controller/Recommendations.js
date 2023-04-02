angular.module('music').controller('Recommendations', function ($scope, $stateParams, Restangular, Playlist, SpotifyAuth, SpotifyRecommendations) {
    // Get artist details
    function callRecommendation() {
        spotify / auth
        // Spotify access token
        var clientId = "a45dfd5652a04284958da68afd1d6ec9";
        var clientSecret = "61ae0ae3987a4d81acc276d86587a0ff";

        // Create a new SpotifyAuth object
        var auth = new SpotifyAuth(clientId, clientSecret);

        // Retrieve an access token
        var accessToken = auth.getAccessToken();

        // Song and artist seeds
        var songSeeds = ['Bad Guy', 'Enemy'];
        var artistSeeds = ['Billie eilish', 'Imagine Dragons'];

        // Create a new SpotifyRecommendations object
        var spotify = new SpotifyRecommendations(accessToken);

        // Call the getRecommendations method and pass in the seeds
        var response = spotify.getRecommendations(songSeeds, artistSeeds);

        // Return the response
        return response;
    }

    var response = callRecommendation();

    $scope.recommend = function () {
        $scope.tracks = response;
    }
})