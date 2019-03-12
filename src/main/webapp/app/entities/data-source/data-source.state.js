(function() {
    'use strict';

    angular
        .module('prototipoRioDoceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('data-source', {
            parent: 'entity',
            url: '/data-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DataSources'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-source/data-sources.html',
                    controller: 'DataSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('data-source-detail', {
            parent: 'data-source',
            url: '/data-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DataSource'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-source/data-source-detail.html',
                    controller: 'DataSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DataSource', function($stateParams, DataSource) {
                    return DataSource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'data-source',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('data-source-detail.edit', {
            parent: 'data-source-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-source/data-source-dialog.html',
                    controller: 'DataSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataSource', function(DataSource) {
                            return DataSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-source.new', {
            parent: 'data-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-source/data-source-dialog.html',
                    controller: 'DataSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                path: null,
                                dtinicial: null,
                                dtfinal: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('data-source', null, { reload: 'data-source' });
                }, function() {
                    $state.go('data-source');
                });
            }]
        })
        .state('data-source.edit', {
            parent: 'data-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-source/data-source-dialog.html',
                    controller: 'DataSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataSource', function(DataSource) {
                            return DataSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-source', null, { reload: 'data-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-source.delete', {
            parent: 'data-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-source/data-source-delete-dialog.html',
                    controller: 'DataSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DataSource', function(DataSource) {
                            return DataSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-source', null, { reload: 'data-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
