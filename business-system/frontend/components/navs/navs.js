(function() {
    let orderItem = angular.module('navs', [])
        .controller('navsCtrl', ['$scope',
            function($scope) {
                $scope.toStatus = function(status) {
                    $scope.$emit('transferStatus', status);
                };
            }
        ]);
})();