(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('GameUserDetailController', GameUserDetailController);

    GameUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GameUser', 'User', 'Game'];

    function GameUserDetailController($scope, $rootScope, $stateParams, previousState, entity, GameUser, User, Game) {
        var vm = this;

        vm.gameUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('basketballApp:gameUserUpdate', function(event, result) {
            vm.gameUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
