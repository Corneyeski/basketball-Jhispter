(function() {
    'use strict';

    angular
        .module('basketballApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('game-user', {
            parent: 'entity',
            url: '/game-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'basketballApp.gameUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game-user/game-users.html',
                    controller: 'GameUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gameUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('game-user-detail', {
            parent: 'entity',
            url: '/game-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'basketballApp.gameUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game-user/game-user-detail.html',
                    controller: 'GameUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gameUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GameUser', function($stateParams, GameUser) {
                    return GameUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'game-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('game-user-detail.edit', {
            parent: 'game-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-user/game-user-dialog.html',
                    controller: 'GameUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GameUser', function(GameUser) {
                            return GameUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game-user.new', {
            parent: 'game-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-user/game-user-dialog.html',
                    controller: 'GameUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                points: null,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('game-user', null, { reload: 'game-user' });
                }, function() {
                    $state.go('game-user');
                });
            }]
        })
        .state('game-user.edit', {
            parent: 'game-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-user/game-user-dialog.html',
                    controller: 'GameUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GameUser', function(GameUser) {
                            return GameUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game-user', null, { reload: 'game-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game-user.delete', {
            parent: 'game-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-user/game-user-delete-dialog.html',
                    controller: 'GameUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GameUser', function(GameUser) {
                            return GameUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game-user', null, { reload: 'game-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
