(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('atividade', {
            parent: 'entity',
            url: '/atividade',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Atividades'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/atividade/atividades.html',
                    controller: 'AtividadeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('atividade-detail', {
            parent: 'atividade',
            url: '/atividade/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Atividade'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/atividade/atividade-detail.html',
                    controller: 'AtividadeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Atividade', function($stateParams, Atividade) {
                    return Atividade.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'atividade',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('atividade-detail.edit', {
            parent: 'atividade-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atividade/atividade-dialog.html',
                    controller: 'AtividadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Atividade', function(Atividade) {
                            return Atividade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('atividade.new', {
            parent: 'atividade',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atividade/atividade-dialog.html',
                    controller: 'AtividadeDialogController',
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
                    $state.go('atividade', null, { reload: 'atividade' });
                }, function() {
                    $state.go('atividade');
                });
            }]
        })
        .state('atividade.edit', {
            parent: 'atividade',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atividade/atividade-dialog.html',
                    controller: 'AtividadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Atividade', function(Atividade) {
                            return Atividade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('atividade', null, { reload: 'atividade' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('atividade.delete', {
            parent: 'atividade',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/atividade/atividade-delete-dialog.html',
                    controller: 'AtividadeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Atividade', function(Atividade) {
                            return Atividade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('atividade', null, { reload: 'atividade' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
