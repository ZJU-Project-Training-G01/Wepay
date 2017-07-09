let app = angular
    .module('myApp', ['oc.lazyLoad', 'ui.router', 'bw.paging']);
app.component('navbar', {
    templateUrl: 'frontend/components/navbar/navbar.html',
});
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('', '/');
    $stateProvider.state('order', {
        url: '/order',
        templateUrl: 'frontend/pages/order/order.html',
        controller: 'orderCtrl',
        resolve: {
            home: function($ocLazyLoad) {
                return $ocLazyLoad.load(['order']);
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
                'frontend/pages/order/order.css',
                'frontend/components/orderItem/orderItem.js',
                'frontend/components/orderItem/orderItem.css',
                'frontend/components/navs/navs.js'
            ]
        }]
    });
});