(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('PropriedadeDialogController', PropriedadeDialogController);

    PropriedadeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Propriedade', 'DataSource'];

    function PropriedadeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Propriedade, DataSource) {
        var vm = this;

        vm.propriedade = entity;
        vm.clear = clear;
        vm.save = save;
        vm.datasources = DataSource.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.propriedade.id !== null) {
                Propriedade.update(vm.propriedade, onSaveSuccess, onSaveError);
            } else {
                Propriedade.save(vm.propriedade, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prototipoRioDoceApp:propriedadeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
