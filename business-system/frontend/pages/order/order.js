(function() {
    var order = angular.module('order', [])
        .component('navs', {
            templateUrl: 'frontend/components/navs/navs.html',
        })
        .component('orderItem', {
            templateUrl: 'frontend/components/orderItem/orderItem.html',
            controller: 'orderItemCtrl'
        })
        .factory('setPricePrecision', function() {
            return function(originalPrice) {
                return '￥' + (originalPrice.toFixed(2)).toString();
            }
        })
        .factory('getDeadline', function() {
            return function(order_time) {
                var deadline = Date.parse(new Date(order_time)) + 7 * 24 * 3600 * 1000 - Date.parse(new Date());
                var dayLevel = 24 * 3600 * 1000;
                var hourLevel = 3600 * 1000;
                var day = Math.floor(deadline / dayLevel);
                var hour = ((deadline - day * dayLevel) / hourLevel).toFixed(0);
                return day.toString() + '天' + hour.toString() + '时';
            }
        }).factory('orderStatusToName', function() {
            return function(order_status)
        })
})();