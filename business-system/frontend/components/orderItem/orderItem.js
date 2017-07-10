(function() {
    angular.module('orderItem', [])
        .controller('orderItemCtrl', ['$scope', '$http', 'setPricePrecision', 'orderServe',
            function($scope, $http, setPricePrecision, orderServe) {
                $scope.pageSize = 10;
                $scope.pageNumber = 1;
                $scope.$on('receiveStatus', function(e, status) {
                    $scope.status = status;
                    $scope.orderHttp($scope.pageNumber);
                });
                $scope.orderHttp = function(pageNumber) {
                    $http({
                        method: 'post',
                        url: 'frontend/static/json/orders.json',
                        data: { pageNumber: pageNumber, pageSize: $scope.pageSize, status: $scope.status }
                    }).then(function(data) {
                        $scope.orders = data.data.data;
                        $scope.total = $scope.orders.length;
                        $scope.total = 22; //this line needs to be deleted when app is built
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
                $scope.orderHttp($scope.pageNumber);
            }
        ]);
})();