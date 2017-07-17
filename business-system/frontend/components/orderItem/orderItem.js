(function() {
    angular.module('orderItem', [])
        .controller('orderItemCtrl', ['$scope', '$http', 'setPricePrecision', 'orderServe', '$rootScope',
            function($scope, $http, setPricePrecision, orderServe, $rootScope) {
                $scope.pageSize = 10;
                $scope.pageNumber = 1;
                $scope.status = -1;
                $scope.$on('receiveStatus', function(e, status) {
                    $scope.status = status;
                    $scope.orderHttp($scope.pageNumber);
                });
                $scope.sendGood = function(item) {
                    $http({
                        url: 'backend/public/DeliveryGoods',
                        method: 'post',
                        data: { orderId: item.orderId }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            item.orderStatus = 1;
                            item.orderName = '买家未收货';
                            item.operation = '等待买家收货';
                        } else if (code > 1) {
                            let errorMsg = data.data.msg;
                            $scope.$emit('transferErrorMsg', errorMsg);
                        } else {
                            $scope.$emit('transferErrorMsg', '未知原因');
                        }
                    })
                }
                $scope.orderHttp = function(pageNumber) {
                    $http({
                        method: 'post',
                        url: 'backend/public/SellersGetOrders',
                        data: { pageNumber: pageNumber, pageSize: $scope.pageSize, status: $scope.status }
                    }).then(function(data) {
                        $scope.orders = data.data.data;
                        $scope.total = data.data.msg;
                        $scope.orders.forEach(function(val) {
                            val.total = val.amount * val.unitPrice;
                            val.unitPrice = setPricePrecision(parseFloat(val.unitPrice));
                            val.total = setPricePrecision(parseFloat(val.total));
                            let { orderName, operation } = orderServe.orderStatusToName(val.orderStatus, val.orderTime);
                            val.operation = operation;
                            val.orderName = orderName;
                            if (val.operation === '订单取消') {
                                val.orderStatus = 4;
                            }
                        });
                    }).then(function(data) {});
                };
                $scope.orderHttp($scope.pageNumber);
            }
        ]);
})();