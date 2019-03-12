(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entidade', {
            parent: 'entity',
            url: '/entidade',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Entidades'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entidade/entidades.html',
                    controller: 'EntidadeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('entidade-detail', {
            parent: 'entidade',
            url: '/entidade/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Entidade'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entidade/entidade-detail.html',
                    controller: 'EntidadeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Entidade', function($stateParams, Entidade) {
                    return Entidade.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'entidade',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('entidade-detail.edit', {
            parent: 'entidade-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidade/entidade-dialog.html',
                    controller: 'EntidadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Entidade', function(Entidade) {
                            return Entidade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entidade.new', {
            parent: 'entidade',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidade/entidade-dialog.html',
                    controller: 'EntidadeDialogController',
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
                    $state.go('entidade', null, { reload: 'entidade' });
                }, function() {
                    $state.go('entidade');
                });
            }]
        })
        .state('entidade.edit', {
            parent: 'entidade',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidade/entidade-dialog.html',
                    controller: 'EntidadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Entidade', function(Entidade) {
                            return Entidade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entidade', null, { reload: 'entidade' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entidade.delete', {
            parent: 'entidade',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidade/entidade-delete-dialog.html',
                    controller: 'EntidadeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Entidade', function(Entidade) {
                            return Entidade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entidade', null, { reload: 'entidade' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
