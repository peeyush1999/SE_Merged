'use strict';

/**
 * Playlist controller.
 */
angular.module('music').controller('Playlist', function($rootScope, $scope, $state, $stateParams, Restangular, Playlist, NamedPlaylist) {
  // Load playlist
  Restangular.one('playlist', $stateParams.id).get().then(function(data) {
    $scope.playlist = data;
  });

  // transfer data between main.js and playlist.js
  // var myData = { song: "Enemy" };
  var data;
  // $rootScope.transfer = data;
  var songs;
  function getSongs(playlistid) {
    Restangular.one('playlist/' + playlistid).get({ id: playlistid }).then(function (response) {
      songs = response;
    });
    // var randomIndex = Math.floor(Math.random() * songs.tracks.length);
    // var randomSong = songs[randomIndex];
    // var song = songs.data;
    // return songs.tracks[randomIndex].title;
    return songs;
  }

  $scope.recommend = function (id) {
    // data = id;
    data = getSongs(id);

    $scope.tracks = [];
    
    for (var i = 0; i < 10; i++) {
      var randomIndex = Math.floor(Math.random() * data.tracks.length);
      var randomSong = data.tracks[randomIndex].title;
      var song;
      Restangular.one('locale/getData').get({ songs: randomSong }).then(function (response) {
        $scope.tracks.push(response);
      });
    }
  }

  //LastFM Recommendations
  $scope.recommendlastfm = function (id) {
    // data = id;
    data = getSongs(id);

    $scope.fmtracks = [];

    for (var i = 0; i < data.tracks.length; i++) {
      var artist = data.tracks[i].artist.name;
      var randomSong = data.tracks[i].title;
      var song;
      Restangular.one('locale/getFmData').get({ songs: randomSong, artist: artist }).then(function (response) {
        $scope.fmtracks.push(response);
        // $scope.fmtracks.push(artist);
      });
    }
  }

  // Play a single track
  $scope.playTrack = function(track) {
    Playlist.removeAndPlay(track);
  };

  // Add a single track to the playlist
  $scope.addTrack = function(track) {
    Playlist.add(track, false);
  };

  // Add all tracks to the playlist in a random order
  $scope.shuffleAllTracks = function() {
    Playlist.addAll(_.shuffle(_.pluck($scope.playlist.tracks, 'id')), false);
  };

  // Play all tracks
  $scope.playAllTracks = function() {
    Playlist.removeAndPlayAll(_.pluck($scope.playlist.tracks, 'id'));
  };

  // Add all tracks to the playlist
  $scope.addAllTracks = function() {
    Playlist.addAll(_.pluck($scope.playlist.tracks, 'id'), false);
  };

  // Like/unlike a track
  $scope.toggleLikeTrack = function(track) {
    Playlist.likeById(track.id, !track.liked);
  };

  // Remove a track
  $scope.removeTrack = function(order) {
    NamedPlaylist.removeTrack($scope.playlist, order).then(function(data) {
      $scope.playlist = data;
    });
  };

  // Delete the playlist
  $scope.remove = function() {
    NamedPlaylist.remove($scope.playlist).then(function() {
      $state.go('main.default');
    });
  };

  // Update UI on track liked
  $scope.$on('track.liked', function(e, trackId, liked) {
    var track = _.findWhere($scope.playlist.tracks, { id: trackId });
    if (track) {
      track.liked = liked;
    }
  });

  // Configuration for track sorting
  $scope.trackSortableOptions = {
    forceHelperSize: true,
    forcePlaceholderSize: true,
    tolerance: 'pointer',
    handle: '.handle',
    containment: 'parent',
    helper: function(e, ui) {
      ui.children().each(function() {
        $(this).width($(this).width());
      });
      return ui;
    },
    stop: function (e, ui) {
      // Send new positions to server
      $scope.$apply(function () {
        NamedPlaylist.moveTrack($scope.playlist, ui.item.attr('data-order'), ui.item.index());
      });
    }
  };
});