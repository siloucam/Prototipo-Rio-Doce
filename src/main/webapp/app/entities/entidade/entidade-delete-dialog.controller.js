(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('EntidadeDeleteController',EntidadeDeleteController);

    EntidadeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Entidade'];

    function EntidadeDeleteController($uibModalInstance, entity, Entidade) {
        var vm = this;

        vm.entidade = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Entidade.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
