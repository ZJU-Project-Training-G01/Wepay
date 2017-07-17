(function() {
    angular.module('order', [])
        .controller('order', ['$scope',
            function($scope) {}
        ])
        .component('navs', {
            templateUrl: 'frontend/components/navs/navs.html',
            controller: 'navsCtrl'
        })
        .component('orderItem', {
            templateUrl: 'frontend/components/orderItem/orderItem.html',
            controller: 'orderItemCtrl'
        })
        .factory('getDeadline', function() {
            return function(orderTime) {
                let deadline = Date.parse(new Date(orderTime)) + 7 * 24 * 3600 * 1000 - Date.parse(new Date());
                if (deadline < 0) {
                    return false;
                }
                let dayLevel = 24 * 3600 * 1000;
                let hourLevel = 3600 * 1000;
                let day = Math.floor(deadline / dayLevel);
                let hour = ((deadline - day * dayLevel) / hourLevel).toFixed(0);
                return '剩余' + day.toString() + '天' + hour.toString() + '时';
            };
        })
        .factory('orderServe', function(getDeadline) {
            return {
                orderStatusToName: function(status, orderTime) {
                    let orderName, operation;
                    switch (parseInt(status)) {
                        case 0:
                            operation = getDeadline(orderTime);
                            if (operation === false) {
                                orderName = '您未及时发货';
                                operation = '订单取消';
                            } else {
                                orderName = '未发货';
                            }
                            break;
                        case 1:
                            orderName = '买家未收货';
                            operation = '等待买家收货';
                            break;
                        case 2:
                            orderName = '买家收货';
                            operation = '订单完成';
                            break;
                        default:
                            orderName = '未知错误';
                            operation = '未知错误';
                            break;
                    }
                    return {
                        orderName: orderName,
                        operation: operation
                    };
                }
            };
        });
})();;(function() {
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
                        } else if (code > 0) {
                            let errorMsg = data.data.msg;
                            $scope.$emit('transferErrorMsg', errorMsg);
                        }
                    });
                };
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
                            val.orderStatus = parseInt(val.orderStatus);
                            val.unitPrice = setPricePrecision(val.unitPrice);
                            val.total = setPricePrecision(val.total);
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
})();;(function() {
    let orderItem = angular.module('navs', [])
        .controller('navsCtrl', ['$scope',
            function($scope) {
                $scope.toStatus = function(status) {
                    $scope.$emit('transferStatus', status);
                };
            }
        ]);
})();