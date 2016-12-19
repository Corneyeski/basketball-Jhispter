(function() {
    'use strict';
    angular
        .module('basketballApp')
        .factory('FavUser', FavUser);

    FavUser.$inject = ['$resource', 'DateUtils'];

    function FavUser ($resource, DateUtils) {
        var resourceUrl =  'api/fav-users/:id';

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
