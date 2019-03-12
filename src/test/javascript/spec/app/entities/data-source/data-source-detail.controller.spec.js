'use strict';

describe('Controller Tests', function() {

    describe('DataSource Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDataSource, MockMetodo, MockEntidade, MockAtividade, MockPropriedade, MockProveniencia;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDataSource = jasmine.createSpy('MockDataSource');
            MockMetodo = jasmine.createSpy('MockMetodo');
            MockEntidade = jasmine.createSpy('MockEntidade');
            MockAtividade = jasmine.createSpy('MockAtividade');
            MockPropriedade = jasmine.createSpy('MockPropriedade');
            MockProveniencia = jasmine.createSpy('MockProveniencia');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DataSource': MockDataSource,
                'Metodo': MockMetodo,
                'Entidade': MockEntidade,
                'Atividade': MockAtividade,
                'Propriedade': MockPropriedade,
                'Proveniencia': MockProveniencia
            };
            createController = function() {
                $injector.get('$controller')("DataSourceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'prototipoRioDoceApp:dataSourceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
