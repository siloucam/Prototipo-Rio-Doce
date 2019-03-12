(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('propriedade', {
            parent: 'entity',
            url: '/propriedade',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Propriedades'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/propriedade/propriedades.html',
                    controller: 'PropriedadeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('propriedade-detail', {
            parent: 'propriedade',
            url: '/propriedade/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Propriedade'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/propriedade/propriedade-detail.html',
                    controller: 'PropriedadeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Propriedade', function($stateParams, Propriedade) {
                    return Propriedade.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'propriedade',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('propriedade-detail.edit', {
            parent: 'propriedade-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/propriedade/propriedade-dialog.html',
                    controller: 'PropriedadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Propriedade', function(Propriedade) {
                            return Propriedade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('propriedade.new', {
            parent: 'propriedade',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/propriedade/propriedade-dialog.html',
                    controller: 'PropriedadeDialogController',
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
                    $state.go('propriedade', null, { reload: 'propriedade' });
                }, function() {
                    $state.go('propriedade');
                });
            }]
        })
        .state('propriedade.edit', {
            parent: 'propriedade',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/propriedade/propriedade-dialog.html',
                    controller: 'PropriedadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Propriedade', function(Propriedade) {
                            return Propriedade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('propriedade', null, { reload: 'propriedade' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('propriedade.delete', {
            parent: 'propriedade',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/propriedade/propriedade-delete-dialog.html',
                    controller: 'PropriedadeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Propriedade', function(Propriedade) {
                            return Propriedade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('propriedade', null, { reload: 'propriedade' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
