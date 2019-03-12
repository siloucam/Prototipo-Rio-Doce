(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('PropriedadeDetailController', PropriedadeDetailController);

    PropriedadeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Propriedade', 'DataSource'];

    function PropriedadeDetailController($scope, $rootScope, $stateParams, previousState, entity, Propriedade, DataSource) {
        var vm = this;

        vm.propriedade = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('prototipoRioDoceApp:propriedadeUpdate', function(event, result) {
            vm.propriedade = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
