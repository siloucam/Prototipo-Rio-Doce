(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('MetodoDetailController', MetodoDetailController);

    MetodoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Metodo', 'DataSource'];

    function MetodoDetailController($scope, $rootScope, $stateParams, previousState, entity, Metodo, DataSource) {
        var vm = this;

        vm.metodo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('prototipoRioDoceApp:metodoUpdate', function(event, result) {
            vm.metodo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
