(function() {
    let app = angular
        .module('myApp', ['oc.lazyLoad', 'ui.router', 'bw.paging', 'ngAnimate', 'angularModalService', 'ngFileUpload', 'ngImgCrop'])
        .run(['$rootScope', function($rootScope) {
            $rootScope.$on('transferErrorMsg', function(e, errorMsg, status) {
                $rootScope.$broadcast('receiveErrorMsg', errorMsg, status);
                if (status === true) {
                    $rootScope.$broadcast('receShowNavbar');
                }
            });
            $rootScope.$on('transferStatus', function(e, status) {
                $rootScope.$broadcast('receiveStatus', status);
            });
            $rootScope.$on('hideNavbar', function(e) {
                $rootScope.$broadcast('receHideNavbar');
            });
            $rootScope.$on('upload', function(e) {
                $rootScope.$broadcast('receUpload');
            });
        }])
        .controller('error', ['$scope', '$window',
            function($scope, $window) {
                $scope.ifError = false;
                $scope.ifSuccess = false;
                $scope.$on('receiveErrorMsg', function(e, errorMsg, ifSuccess) {
                    $scope.errorMsg = errorMsg;
                    if (!ifSuccess) {
                        $scope.ifError = true;
                    } else {
                        $scope.ifSuccess = true;
                        if (ifSuccess === 'modal') {
                            $scope.close = function() {
                                $window.location.reload();
                            };
                        }
                    }
                });
            }
        ])
        .controller('navbar', ['$scope', '$http', '$location',
            function($scope, $http, $location) {
                $scope.ifNavbar = true;
                $scope.$on('receHideNavbar', function(e) {
                    $scope.ifNavbar = false;
                });
                $scope.$on('receShowNavbar', function(e) {
                    $scope.ifNavbar = true;
                });
                $scope.loginOut = function() {
                    $http({
                        url: 'frontend/static/jsons/loginOut.json',
                        method: 'post',
                        data: {}
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '登出成功', true);
                            $location.path('/');
                        }
                    });
                };
            }
        ])
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
                return '￥' + (parseFloat(originalPrice).toFixed(2)).toString();
            };
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
                        return $ocLazyLoad.load(['login']);
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
                        return $ocLazyLoad.load(['good']);
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
                        return $ocLazyLoad.load(['goodUpload']);
                    }
                }
            })
            .state('goodUpdate', {
                url: '/goodUpload/{ifUpdate}',
                template: '<good-upload></good-upload>',
                resolve: {
                    goodUpload: function($ocLazyLoad) {
                        return $ocLazyLoad.load(['goodUpload']);
                    }
                }
            });
    }]);
    app.config(function($ocLazyLoadProvider) {
        $ocLazyLoadProvider.config({
            debug: true,
            modules: [{
                    name: 'register',
                    files: [
                        'frontend/pages/register/register.min.js',
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
                        'frontend/pages/user/user.css',
                        'frontend/components/bind/bind.css',
                        'frontend/components/out/out.css',
                        'frontend/components/in/in.css',
                        'frontend/pages/user/asset/user.min.js',
                        'frontend/pages/upload/upload.css',
                        'frontend/pages/upload/upload.js'
                    ]
                },
                {
                    name: 'order',
                    files: [
                        'frontend/pages/order/asset/order.js',
                        'frontend/components/orderItem/orderItem.css',
                    ]
                },
                {
                    name: 'good',
                    files: [
                        'frontend/pages/good/good.css',
                        'frontend/components/goodItem/goodItem.css',
                        'frontend/components/goodSearch/goodSearch.css',
                        'frontend/pages/good/asset/good.min.js'
                    ]
                },
                {
                    name: 'goodDetail',
                    files: [
                        'frontend/pages/goodDetail/goodDetail.min.css',
                        'frontend/pages/goodDetail/goodDetail.js',
                    ]
                },
                {
                    name: 'goodUpload',
                    files: [
                        'frontend/pages/goodUpload/goodUpload.css',
                        'frontend/pages/goodUpload/goodUpload.js',
                        'frontend/pages/upload/upload.css',
                        'frontend/pages/upload/upload.js'
                    ]
                }
            ]
        });
    });
})();