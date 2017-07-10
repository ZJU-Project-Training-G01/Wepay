let app = angular
    .module('myApp', ['oc.lazyLoad', 'ui.router', 'bw.paging']);
app.component('navbar', {
        templateUrl: 'frontend/components/navbar/navbar.html',
    })
    .component('order', {
        templateUrl: 'frontend/pages/order/order.html',
        controller: 'order'
    })
    .component('good', {
        templateUrl: 'frontend/pages/good/good.html',
        controller: 'good'
    })
    .component('goodDetail', {
        templateUrl: 'frontend/pages/goodDetail/goodDetail.html',
        controller: 'goodDetail'
    })
    .factory('setPricePrecision', function() {
        return function(originalPrice) {
            return '￥' + (originalPrice.toFixed(2)).toString();
        }
    });
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('', '/');
    $stateProvider.state('order', {
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
            template: '<goodDetail></goodDetail>',
            resolve: {
                goodDetail: function($ocLazyLoad) {
                    return $ocLazyLoad.load(['goodDetail']);
                }
            }
        })
}])
app.config(function($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true,
        modules: [{
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
                    'frontend/pages/goodDetail/goodDetail.js',
                    'frontend/components/goodItemDetail/goodItemDetail.js'
                ]
            }
        ]
    });
});