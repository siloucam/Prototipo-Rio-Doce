(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('ProvenienciaDialogController', ProvenienciaDialogController);

    ProvenienciaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proveniencia', 'DataSource'];

    function ProvenienciaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Proveniencia, DataSource) {
        var vm = this;

        vm.proveniencia = entity;
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
            if (vm.proveniencia.id !== null) {
                Proveniencia.update(vm.proveniencia, onSaveSuccess, onSaveError);
            } else {
                Proveniencia.save(vm.proveniencia, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prototipoRioDoceApp:provenienciaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
