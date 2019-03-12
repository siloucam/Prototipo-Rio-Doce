(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('EntidadeDetailController', EntidadeDetailController);

    EntidadeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Entidade', 'DataSource'];

    function EntidadeDetailController($scope, $rootScope, $stateParams, previousState, entity, Entidade, DataSource) {
        var vm = this;

        vm.entidade = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('prototipoRioDoceApp:entidadeUpdate', function(event, result) {
            vm.entidade = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
