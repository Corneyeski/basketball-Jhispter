(function() {
    'use strict';
    angular
        .module('basketballApp')
        .factory('Game', Game);

    Game.$inject = ['$resource', 'DateUtils'];

    function Game ($resource, DateUtils) {
        var resourceUrl =  'api/games/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.times = DateUtils.convertDateTimeFromServer(data.times);
                        data.timef = DateUtils.convertDateTimeFromServer(data.timef);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
