(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('GameUserController', GameUserController);

    GameUserController.$inject = ['$scope', '$state', 'GameUser'];

    function GameUserController ($scope, $state, GameUser) {
        var vm = this;

        vm.gameUsers = [];

        loadAll();

        function loadAll() {
            GameUser.query(function(result) {
                vm.gameUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
