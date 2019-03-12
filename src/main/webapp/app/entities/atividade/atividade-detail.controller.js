(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('AtividadeDetailController', AtividadeDetailController);

    AtividadeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Atividade', 'DataSource'];

    function AtividadeDetailController($scope, $rootScope, $stateParams, previousState, entity, Atividade, DataSource) {
        var vm = this;

        vm.atividade = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('prototipoRioDoceApp:atividadeUpdate', function(event, result) {
            vm.atividade = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
