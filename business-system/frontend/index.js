var app = angular
    .module('myApp', ['oc.lazyLoad', 'ui.router']);
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('', '/');
    $stateProvider.state('sidebar', {
        url: '/',
        templateUrl: 'frontend/components/sidebar/sidebar.html',
        resolve: {
            home: function($ocLazyLoad) {
                return $ocLazyLoad.load(['sidebar']);
            }
        }


    })
}])
app.config(function($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true,
        modules: [{
            name: 'sidebar',
            files: [
                'frontend/node_modules/sidebarjs/dist/sidebarjs.css',
                'frontend/components/sidebar/sidebar.css',
                "frontend/node_modules/angular-sidebarjs/dist/angular-sidebarjs.js",
                'frontend/components/sidebar/sidebar.js'
            ]
        }]
    });
});