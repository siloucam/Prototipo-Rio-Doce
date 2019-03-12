(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('metodo', {
            parent: 'entity',
            url: '/metodo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Metodos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metodo/metodos.html',
                    controller: 'MetodoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('metodo-detail', {
            parent: 'metodo',
            url: '/metodo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Metodo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metodo/metodo-detail.html',
                    controller: 'MetodoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Metodo', function($stateParams, Metodo) {
                    return Metodo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'metodo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('metodo-detail.edit', {
            parent: 'metodo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metodo/metodo-dialog.html',
                    controller: 'MetodoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Metodo', function(Metodo) {
                            return Metodo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metodo.new', {
            parent: 'metodo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metodo/metodo-dialog.html',
                    controller: 'MetodoDialogController',
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
                    $state.go('metodo', null, { reload: 'metodo' });
                }, function() {
                    $state.go('metodo');
                });
            }]
        })
        .state('metodo.edit', {
            parent: 'metodo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metodo/metodo-dialog.html',
                    controller: 'MetodoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Metodo', function(Metodo) {
                            return Metodo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metodo', null, { reload: 'metodo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metodo.delete', {
            parent: 'metodo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metodo/metodo-delete-dialog.html',
                    controller: 'MetodoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Metodo', function(Metodo) {
                            return Metodo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metodo', null, { reload: 'metodo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
