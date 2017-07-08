(function() {
    var orderItem = angular.module('orderItem', [])
        .controller('orderItemCtrl', ['$scope', '$http', 'setPricePrecision', function($scope, $http, setPricePrecision) {
            $scope.pageSize = 10;
            $http({
                method: 'post',
                url: 'frontend/static/json/orders.json',
                data: { pageSize: $scope.pageSize }
            }).then(function(data) {
                $scope.orders = data.data.data;
                $scope.orders.forEach(function(val) {
                    val.total = val.amount * val.unit_price;
                    val.unit_price = setPricePrecision(val.unit_price);
                    val.total = setPricePrecision(val.total);
                });
            }).then(function(data) {});

        }]);
})();