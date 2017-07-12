(function() {
    let app = angular
        .module('myApp', ['oc.lazyLoad', 'ui.router', 'bw.paging'])
        .run(['$rootScope', function($rootScope) {
            $rootScope.$on('transferErrorMsg', function(e, errorMsg) {
                $rootScope.$broadcast('receiveErrorMsg', errorMsg);
            });
        }])
        .controller('error', ['$scope', function($scope) {
            $scope.ifError = false;
            $scope.$on('receiveErrorMsg', function(e, errorMsg) {
                $scope.errorMsg = errorMsg;
                $scope.ifError = true;
            })
        }])
        .component('user', {
            templateUrl: 'frontend/pages/user/user.html',
            controller: 'user'
        })
        .component('navbar', {
            templateUrl: 'frontend/components/navbar/navbar.html',
        })
        .component('error', {
            templateUrl: 'frontend/components/error/error.html',
            controller: 'error'
        })
        .component('order', {
            templateUrl: 'frontend/pages/order/order.html',
        })
        .component('good', {
            templateUrl: 'frontend/pages/good/good.html',
            controller: 'good'
        })
        .component('goodDetail', {
            templateUrl: 'frontend/pages/goodDetail/goodDetail.html',
            controller: 'goodDetail'
        })
        .component('goodUpload', {
            templateUrl: 'frontend/pages/goodUpload/goodUpload.html',
            controller: 'goodUpload'
        })
        .factory('setPricePrecision', function() {
            return function(originalPrice) {
                return '￥' + (originalPrice.toFixed(2)).toString();
            }
        });
    app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.when('', '/');
        $stateProvider
            .state('user', {
                url: '/user',
                template: '<user></user>',
                resolve: {
                    user: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['user']);
                    }
                }
            })
            .state('order', {
                url: '/order',
                template: '<order></order>',
                resolve: {
                    order: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['order']);
                    }
                }
            })
            .state('good', {
                url: '/good',
                template: '<good></good>',
                resolve: {
                    good: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['good'])
                    }
                }
            })
            .state('goodDetail', {
                url: '/good/{goodId}',
                template: '<good-detail></good-detail>',
                resolve: {
                    goodDetail: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['goodDetail']);
                    }
                }
            })
            .state('goodUpload', {
                url: '/goodUpload',
                template: '<good-upload></good-upload>',
                resolve: {
                    goodUpload: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['goodUpload'])
                    }
                }
            })
            .state('goodUpdate', {
                url: '/goodUpload/{ifUpdate}',
                template: '<good-upload></good-upload>',
                resolve: {
                    goodUpload: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['goodUpload'])
                    }
                }
            })
    }])
    app.config(function($ocLazyLoadProvider) {
        $ocLazyLoadProvider.config({
            debug: true,
            modules: [{
                    name: 'user',
                    files: [
                        'frontend/pages/user/user.js',
                        'frontend/pages/user/user.css'
                    ]
                },
                {
                    name: 'order',
                    files: [
                        'frontend/pages/order/order.js',
                        'frontend/components/orderItem/orderItem.js',
                        'frontend/components/orderItem/orderItem.css',
                        'frontend/components/navs/navs.js'
                    ]
                },
                {
                    name: 'good',
                    files: [
                        'frontend/pages/good/good.css',
                        'frontend/pages/good/good.js',
                        'frontend/components/goodItem/goodItem.css',
                        'frontend/components/goodItem/goodItem.js',
                        'frontend/components/goodSearch/goodSearch.css',
                        'frontend/components/goodSearch/goodSearch.js'
                    ]
                },
                {
                    name: 'goodDetail',
                    files: [
                        'frontend/pages/goodDetail/goodDetail.css',
                        'frontend/pages/goodDetail/goodDetail.js',
                    ]
                },
                {
                    name: 'goodUpload',
                    files: [
                        'frontend/pages/goodUpload/goodUpload.css',
                        'frontend/pages/goodUpload/goodUpload.js'
                    ]
                }
            ]
        });
    });
})();