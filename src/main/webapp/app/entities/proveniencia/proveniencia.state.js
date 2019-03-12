(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('proveniencia', {
            parent: 'entity',
            url: '/proveniencia',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Proveniencias'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proveniencia/proveniencias.html',
                    controller: 'ProvenienciaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('proveniencia-detail', {
            parent: 'proveniencia',
            url: '/proveniencia/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Proveniencia'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proveniencia/proveniencia-detail.html',
                    controller: 'ProvenienciaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Proveniencia', function($stateParams, Proveniencia) {
                    return Proveniencia.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'proveniencia',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('proveniencia-detail.edit', {
            parent: 'proveniencia-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proveniencia/proveniencia-dialog.html',
                    controller: 'ProvenienciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Proveniencia', function(Proveniencia) {
                            return Proveniencia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('proveniencia.new', {
            parent: 'proveniencia',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proveniencia/proveniencia-dialog.html',
                    controller: 'ProvenienciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('proveniencia', null, { reload: 'proveniencia' });
                }, function() {
                    $state.go('proveniencia');
                });
            }]
        })
        .state('proveniencia.edit', {
            parent: 'proveniencia',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proveniencia/proveniencia-dialog.html',
                    controller: 'ProvenienciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Proveniencia', function(Proveniencia) {
                            return Proveniencia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('proveniencia', null, { reload: 'proveniencia' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('proveniencia.delete', {
            parent: 'proveniencia',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proveniencia/proveniencia-delete-dialog.html',
                    controller: 'ProvenienciaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Proveniencia', function(Proveniencia) {
                            return Proveniencia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('proveniencia', null, { reload: 'proveniencia' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
