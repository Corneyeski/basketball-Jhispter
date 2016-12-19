(function() {
    'use strict';

    angular
        .module('basketballApp')
        .controller('FavUserDialogController', FavUserDialogController);

    FavUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavUser', 'User', 'Player'];

    function FavUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FavUser, User, Player) {
        var vm = this;

        vm.favUser = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.players = Player.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.favUser.id !== null) {
                FavUser.update(vm.favUser, onSaveSuccess, onSaveError);
            } else {
                FavUser.save(vm.favUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('basketballApp:favUserUpdate', result);
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
