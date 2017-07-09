(function() {
    let orderItem = angular.module('navs', [])
        .controller('navsCtrl', ['$scope', '$rootScope',
            function($scope, $rootScope) {
                $scope.toStatus = function(status) {
                    $scope.$emit('transferStatus', status);
                }
            }
        ]);
})();