(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('FavUserController', FavUserController);

    FavUserController.$inject = ['$scope', '$state', 'FavUser'];

    function FavUserController ($scope, $state, FavUser) {
        var vm = this;

        vm.favUsers = [];

        loadAll();

        function loadAll() {
            FavUser.query(function(result) {
                vm.favUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
