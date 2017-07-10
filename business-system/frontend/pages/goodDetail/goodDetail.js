(function() {
    angular.module('goodDetail', [])
        .controller('goodDetail', ['$scope', function($scope) {
            console.log('zz');
        }])
        .component('goodItemDetail', {
            templateUrl: 'frontend/components/goodItemDetail/goodItemDetail.html',
            controller: 'goodItemDetailController'
        });

})();