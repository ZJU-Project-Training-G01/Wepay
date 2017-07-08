(function() {
    let orderItem = angular.module('orderItem', [])
        .controller('orderItemCtrl', ['$scope', '$http', 'setPricePrecision', 'orderServe',
            function($scope, $http, setPricePrecision, orderServe) {
                $scope.pageSize = 10;
                $scope.pageNumber = 1;
                let orderHttp = function(pageNumber) {
                    $http({
                        method: 'post',
                        url: 'frontend/static/json/orders.json',
                        data: { pageNumber: $scope.pageNumber, pageSize: $scope.pageSize }
                    }).then(function(data) {
                        $scope.orders = data.data.data;
                        $scope.orders.forEach(function(val) {
                            val.total = val.amount * val.unitPrice;
                            val.unitPrice = setPricePrecision(val.unitPrice);
                            val.total = setPricePrecision(val.total);
                            let { orderName, operation } = orderServe.orderStatusToName(val.orderStatus, val.orderTime);
                            val.operation = operation;
                            val.orderName = orderName;
                        });
                    }).then(function(data) {});
                };
                orderHttp($scope.pageNumber);
            }
        ]);
})();