angular.module('music').controller('Recommendations', function ($scope, $stateParams, Restangular, Playlist, SpotifyAuth, SpotifyRecommendations) {
    // Get artist details



    $scope.recommend = function () {
        $scope.tracks = "check";
        Restangular.one("user/spotifyRecommendation").get({ songs: "songs sample", artist: "artist sample" }).then(function (response) {
            $scope.tracks = "response";
        })

    }
})