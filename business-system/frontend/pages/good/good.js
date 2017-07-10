(function() {
    angular.module('good', [])
        .controller('good', ['$scope', function($scope) {}])
        .component('goodItem', {
            templateUrl: 'frontend/components/goodItem/goodItem.html',
            controller: 'goodItemCtrl'
        })
        .component('goodSearch', {
            templateUrl: 'frontend/components/goodSearch/goodSearch.html',
            controller: 'goodSearchCtrl'
        });
})();