(function() {
    'use strict';
    angular
        .module('basketballApp')
        .factory('GameUser', GameUser);

    GameUser.$inject = ['$resource', 'DateUtils'];

    function GameUser ($resource, DateUtils) {
        var resourceUrl =  'api/game-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
