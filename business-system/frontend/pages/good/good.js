(function() {
    angular.module('good', [])
        .controller('good', ['$scope',
            function($scope) {
                $scope.$on('transferKeyword', function(e, keyword) {
                    $scope.$broadcast('receiveKeyword', keyword);
                })
            }
        ])
        .component('goodItem', {
            templateUrl: 'frontend/components/goodItem/goodItem.html',
            controller: 'goodItemCtrl'
        })
        .component('goodSearch', {
            templateUrl: 'frontend/components/goodSearch/goodSearch.html',
            controller: 'goodSearchCtrl'
        });


})();