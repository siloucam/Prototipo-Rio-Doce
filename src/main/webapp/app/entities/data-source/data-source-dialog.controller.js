(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('DataSourceDialogController', DataSourceDialogController);

    DataSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DataSource', 'Metodo', 'Entidade', 'Atividade', 'Propriedade', 'Proveniencia'];

    function DataSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DataSource, Metodo, Entidade, Atividade, Propriedade, Proveniencia) {
        var vm = this;

        vm.dataSource = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.metodos = Metodo.query();
        vm.entidades = Entidade.query();
        vm.atividades = Atividade.query();
        vm.propriedades = Propriedade.query();
        vm.proveniencias = Proveniencia.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dataSource.id !== null) {
                DataSource.update(vm.dataSource, onSaveSuccess, onSaveError);
            } else {
                DataSource.save(vm.dataSource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('prototipoRioDoceApp:dataSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dtinicial = false;
        vm.datePickerOpenStatus.dtfinal = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
