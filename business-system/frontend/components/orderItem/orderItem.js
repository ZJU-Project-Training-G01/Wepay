(function() {
    var orderItem = angular.module('orderItem', [])
        .controller('orderItemCtrl', ['$scope,$http', function($scope, $http) {
            $scope.pageSize = 10;
            $http({
                method: 'post',
                url: 'frontend/staic/json/orders.json',
                data: { pageSize: $scope.pageSize }
            }).success(function(data) {
                $scope.orders = data.data;
            });
        }]);
})();