(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('PropriedadeDeleteController',PropriedadeDeleteController);

    PropriedadeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Propriedade'];

    function PropriedadeDeleteController($uibModalInstance, entity, Propriedade) {
        var vm = this;

        vm.propriedade = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Propriedade.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
