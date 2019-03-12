(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('ProvenienciaDeleteController',ProvenienciaDeleteController);

    ProvenienciaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Proveniencia'];

    function ProvenienciaDeleteController($uibModalInstance, entity, Proveniencia) {
        var vm = this;

        vm.proveniencia = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Proveniencia.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
