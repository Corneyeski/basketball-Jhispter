(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('FavUserDetailController', FavUserDetailController);

    FavUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FavUser', 'User', 'Player'];

    function FavUserDetailController($scope, $rootScope, $stateParams, previousState, entity, FavUser, User, Player) {
        var vm = this;

        vm.favUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('basketballApp:favUserUpdate', function(event, result) {
            vm.favUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
