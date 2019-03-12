(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('MetodoDialogController', MetodoDialogController);

    MetodoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Metodo', 'DataSource'];

    function MetodoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Metodo, DataSource) {
        var vm = this;

        vm.metodo = entity;
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
            if (vm.metodo.id !== null) {
                Metodo.update(vm.metodo, onSaveSuccess, onSaveError);
            } else {
                Metodo.save(vm.metodo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prototipoRioDoceApp:metodoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
