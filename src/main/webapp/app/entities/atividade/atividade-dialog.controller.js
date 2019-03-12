(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('AtividadeDialogController', AtividadeDialogController);

    AtividadeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Atividade', 'DataSource'];

    function AtividadeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Atividade, DataSource) {
        var vm = this;

        vm.atividade = entity;
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
            if (vm.atividade.id !== null) {
                Atividade.update(vm.atividade, onSaveSuccess, onSaveError);
            } else {
                Atividade.save(vm.atividade, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prototipoRioDoceApp:atividadeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
