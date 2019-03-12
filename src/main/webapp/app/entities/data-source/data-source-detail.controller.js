(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .controller('DataSourceDetailController', DataSourceDetailController);

    DataSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DataSource', 'Metodo', 'Entidade', 'Atividade', 'Propriedade', 'Proveniencia'];

    function DataSourceDetailController($scope, $rootScope, $stateParams, previousState, entity, DataSource, Metodo, Entidade, Atividade, Propriedade, Proveniencia) {
        var vm = this;

        vm.dataSource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('prototipoRioDoceApp:dataSourceUpdate', function(event, result) {
            vm.dataSource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
