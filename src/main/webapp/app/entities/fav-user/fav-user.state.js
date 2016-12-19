(function() {
    'use strict';

    angular
        .module('basketballApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fav-user', {
            parent: 'entity',
            url: '/fav-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'basketballApp.favUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fav-user/fav-users.html',
                    controller: 'FavUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fav-user-detail', {
            parent: 'entity',
            url: '/fav-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'basketballApp.favUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fav-user/fav-user-detail.html',
                    controller: 'FavUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FavUser', function($stateParams, FavUser) {
                    return FavUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fav-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fav-user-detail.edit', {
            parent: 'fav-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fav-user/fav-user-dialog.html',
                    controller: 'FavUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FavUser', function(FavUser) {
                            return FavUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fav-user.new', {
            parent: 'fav-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fav-user/fav-user-dialog.html',
                    controller: 'FavUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                liked: null,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fav-user', null, { reload: 'fav-user' });
                }, function() {
                    $state.go('fav-user');
                });
            }]
        })
        .state('fav-user.edit', {
            parent: 'fav-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fav-user/fav-user-dialog.html',
                    controller: 'FavUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FavUser', function(FavUser) {
                            return FavUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fav-user', null, { reload: 'fav-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fav-user.delete', {
            parent: 'fav-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fav-user/fav-user-delete-dialog.html',
                    controller: 'FavUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FavUser', function(FavUser) {
                            return FavUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fav-user', null, { reload: 'fav-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
