(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('GameUserDeleteController',GameUserDeleteController);

    GameUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'GameUser'];

    function GameUserDeleteController($uibModalInstance, entity, GameUser) {
        var vm = this;

        vm.gameUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GameUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
