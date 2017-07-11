(function() {
    angular.module('order', [])
        .controller('order', ['$scope',
            function($scope) {
                $scope.$on('transferStatus', function(e, status) {
                    $scope.$broadcast('receiveStatus', status);
                });
            }
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
                let dayLevel = 24 * 3600 * 1000;
                let hourLevel = 3600 * 1000;
                let day = Math.floor(deadline / dayLevel);
                let hour = ((deadline - day * dayLevel) / hourLevel).toFixed(0);
                return '剩余' + day.toString() + '天' + hour.toString() + '时';
            }
        })
        .factory('orderServe', function(getDeadline) {
            return {
                orderStatusToName: function(status, orderTime) {
                    let orderName, operation;
                    switch (status) {
                        case 0:
                            orderName = '未发货';
                            operation = getDeadline(orderTime);
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
                    }
                }
            }
        })
})();