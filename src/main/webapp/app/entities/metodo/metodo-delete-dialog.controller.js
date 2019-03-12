(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('MetodoDeleteController',MetodoDeleteController);

    MetodoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Metodo'];

    function MetodoDeleteController($uibModalInstance, entity, Metodo) {
        var vm = this;

        vm.metodo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Metodo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
