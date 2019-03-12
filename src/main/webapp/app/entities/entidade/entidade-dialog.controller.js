(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('EntidadeDialogController', EntidadeDialogController);

    EntidadeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Entidade', 'DataSource'];

    function EntidadeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Entidade, DataSource) {
        var vm = this;

        vm.entidade = entity;
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
            if (vm.entidade.id !== null) {
                Entidade.update(vm.entidade, onSaveSuccess, onSaveError);
            } else {
                Entidade.save(vm.entidade, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prototipoRioDoceApp:entidadeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
