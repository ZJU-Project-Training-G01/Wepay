var app = angular
    .module('myApp', ['oc.lazyLoad']);
app.directive('mynavbar', function() {
    return {
        template: '<div>saa</div>'
    }
}).directive('mynavbar2', function() {
    return {
        template: '<div>hhha</div>'
    }
});
app.config(function($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true,
        modules: [{
            name: 'sidebar',
            files: [
                'frontend/components/sidebar/sidebar.css',
                'frontend/components/sidebar/sidebar.js'
            ]
        }]
    });
});
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('', '/');
    $stateProvider.this.state('sidebar', {
        url: '/',
        templateUrl: 'frontend/components/sidebar/sidebar.html',
        controller: 'sidebar',
        resolve: {
            home: function($ocLazyLoad) {
                return $ocLazyLoad.load(['sidebar']);
            }
        }


    })
}])