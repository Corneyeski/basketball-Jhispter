(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('FavUserDeleteController',FavUserDeleteController);

    FavUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'FavUser'];

    function FavUserDeleteController($uibModalInstance, entity, FavUser) {
        var vm = this;

        vm.favUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FavUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
