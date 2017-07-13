(function() {
    let app = angular
        .module('myApp', ['oc.lazyLoad', 'ui.router', 'bw.paging'])
        .run(['$rootScope', function($rootScope) {
            $rootScope.$on('transferErrorMsg', function(e, errorMsg, status) {
                $rootScope.$broadcast('receiveErrorMsg', errorMsg, status);
                if (status === true) {
                    $rootScope.$broadcast('receShowNavbar');
                }
            });
            $rootScope.$on('hideNavbar', function(e) {
                $rootScope.$broadcast('receHideNavbar');
            });
        }])
        .controller('error', ['$scope', function($scope) {
            $scope.ifError = false;
            $scope.ifSuccess = false;
            $scope.$on('receiveErrorMsg', function(e, errorMsg, ifSuccess) {
                $scope.errorMsg = errorMsg;
                if (!ifSuccess) {
                    $scope.ifError = true;
                } else {
                    $scope.ifSuccess = true;
                }
            })
        }])
        .controller('navbar', ['$scope', function($scope) {
            $scope.ifNavbar = true;
            $scope.$on('receHideNavbar', function(e) {
                $scope.ifNavbar = false;
            });
            $scope.$on('receShowNavbar', function(e) {
                $scope.ifNavbar = true;
            });
        }])
        .component('register', {
            templateUrl: 'frontend/pages/register/register.html',
            controller: 'register'
        })
        .component('login', {
            templateUrl: 'frontend/pages/login/login.html',
            controller: 'login'
        })
        .component('user', {
            templateUrl: 'frontend/pages/user/user.html',
            controller: 'user'
        })
        .component('navbar', {
            templateUrl: 'frontend/components/navbar/navbar.html',
            controller: 'navbar',
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
            .state('register', {
                url: '/register',
                template: '<register></register>',
                resolve: {
                    register: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['register']);
                    }
                }
            })
            .state('login', {
                url: '/',
                template: '<login></login>',
                resolve: {
                    login: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['login'])
                    }
                }
            })
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
                    name: 'register',
                    files: [
                        'frontend/pages/register/register.js',
                        'frontend/pages/register/register.css',
                        'frontend/components/loginNavbar/loginNavbar.css'
                    ]
                },
                {
                    name: 'login',
                    files: [
                        'frontend/pages/login/login.js',
                        'frontend/pages/login/login.css',
                        'frontend/components/loginNavbar/loginNavbar.css'
                    ]
                },
                {
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