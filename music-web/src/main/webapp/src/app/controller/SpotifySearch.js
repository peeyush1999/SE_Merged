angular.module('music').controller('SpotifySearch', function ($rootScope, $scope, $http, $state, $dialog, User, Playlist, NamedPlaylist, Websocket, Restangular) {


    var ACCESS_TOKEN = '';
    var CLIENT_ID = 'a45dfd5652a04284958da68afd1d6ec9';
    var CLIENT_SECRET = '61ae0ae3987a4d81acc276d86587a0ff'
    var baseUrl = 'https://api.spotify.com/v1/search';

    $scope.tracks = [];

    function search_by_song(song) {
        var params = {
            q: song,
            type: 'track'
        };

        var url = baseUrl + '?' + $.param(params);

        return $http.get(url, {
            headers: {
                'Authorization': 'Bearer ' + ACCESS_TOKEN,
                'Content-Type': 'application/json'
            }
        })
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function get_access_token() {
        var url = 'https://accounts.spotify.com/api/token';
        var data = {
            grant_type: 'client_credentials'
        };
        var headers = {
            'Authorization': 'Basic ' + btoa(CLIENT_ID + ':' + CLIENT_SECRET),
            'Content-Type': 'application/x-www-form-urlencoded'
        };

        return $http.post(url, $.param(data), { headers: headers })
            .then(function (response) {
                ACCESS_TOKEN = response.data.access_token;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    $scope.search = function () {
        get_access_token().then(function () {
            search_by_song($scope.searchQuery).then(function (data) {
                $scope.tracks = data.tracks.items;
            });
        });
    }
});