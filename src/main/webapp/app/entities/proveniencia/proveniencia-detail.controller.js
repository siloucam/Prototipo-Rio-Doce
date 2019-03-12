(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('ProvenienciaDetailController', ProvenienciaDetailController);

    ProvenienciaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Proveniencia', 'DataSource'];

    function ProvenienciaDetailController($scope, $rootScope, $stateParams, previousState, entity, Proveniencia, DataSource) {
        var vm = this;

        vm.proveniencia = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('prototipoRioDoceApp:provenienciaUpdate', function(event, result) {
            vm.proveniencia = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
