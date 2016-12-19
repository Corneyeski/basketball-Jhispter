(function() {
    'use strict';
    angular
        .module('basketballApp')
        .factory('Team', Team);

    Team.$inject = ['$resource', 'DateUtils'];

    function Team ($resource, DateUtils) {
        var resourceUrl =  'api/teams/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fundate = DateUtils.convertLocalDateFromServer(data.fundate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fundate = DateUtils.convertLocalDateToServer(copy.fundate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fundate = DateUtils.convertLocalDateToServer(copy.fundate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
