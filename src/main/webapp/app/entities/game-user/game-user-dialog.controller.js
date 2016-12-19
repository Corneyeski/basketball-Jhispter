(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('GameUserDialogController', GameUserDialogController);

    GameUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GameUser', 'User', 'Game'];

    function GameUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GameUser, User, Game) {
        var vm = this;

        vm.gameUser = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.games = Game.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gameUser.id !== null) {
                GameUser.update(vm.gameUser, onSaveSuccess, onSaveError);
            } else {
                GameUser.save(vm.gameUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('basketballApp:gameUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
