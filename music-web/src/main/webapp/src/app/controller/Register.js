// 'use strict';

/**
 * Register controller.
 */
angular.module('music').controller('Register', function ($rootScope, $scope, $state, $dialog, User, Playlist, NamedPlaylist, Websocket, Restangular) {
    $scope.register = function (user) {
        promise = Restangular
            .one('user')
            .put($scope.user);
        
            saveUserCookie();
            console.log(user);
    }
});